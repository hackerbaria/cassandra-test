package com.arbin.aal.service;

import com.arbin.aal.entity.Tutorial;
import com.datastax.oss.driver.api.core.cql.PagingState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.CriteriaDefinition;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class TutorialService {

    @Autowired
    private CassandraTemplate cassandraTemplate;

    public Slice<Tutorial> getTutorialsByTitle(String fieldName, String textSearch, Pageable pageable, String pagingState) {
        List<CriteriaDefinition> criteriaList = new ArrayList<>();


        if (textSearch != null && !textSearch.isEmpty()) {
            criteriaList.add(Criteria.where(fieldName).like(textSearch + "%"));
        }

        CassandraPageRequest pageRequest;
        if (pagingState != null && !pagingState.isEmpty()) {
            ByteBuffer byteBuffer = com.datastax.oss.protocol.internal.util.Bytes.fromHexString(pagingState);
            pageRequest = CassandraPageRequest.of(pageable, byteBuffer);
        } else {
            pageRequest = CassandraPageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        }

        /*Query query = Query.query(criteriaList).pageRequest(pageRequest);*/
        // since CQL is not support OR, we have to workaround

        /*Query query;
        if (textSearch != null && !textSearch.isEmpty()) {
            List<Slice<Tutorial>> slices = new ArrayList<>();
            // Query for each field separately
            slices.add(queryForField("title", textSearch, pageRequest));
            slices.add(queryForField("description", textSearch, pageRequest));
            // Combine results from all slices
            return combineSlices(slices, pageable);
        } else {
            query = Query.query(criteriaList).pageRequest(pageRequest);
        }*/

        Query query = Query.query(criteriaList).pageRequest(pageRequest);
        Slice<Tutorial> slice = cassandraTemplate.slice(query, Tutorial.class);


        if (slice.getPageable() instanceof CassandraPageRequest) {
            ByteBuffer byteBuffer = ((CassandraPageRequest) slice.getPageable()).getPagingState();
            pagingState = com.datastax.oss.protocol.internal.util.Bytes.toHexString(byteBuffer);
        }




        return new CassandraSliceImpl<>(slice.getContent(), pageable, slice.hasNext(), pagingState);
    }

    private Slice<Tutorial> queryForField(String fieldName, String textSearch, CassandraPageRequest pageRequest) {
        Query query = Query.query(Criteria.where(fieldName).like(textSearch + "%")).pageRequest(pageRequest);
        return cassandraTemplate.slice(query, Tutorial.class);
    }

    private Slice<Tutorial> combineSlices(List<Slice<Tutorial>> slices, Pageable pageable) {
        List<Tutorial> combinedContent = new ArrayList<>();
        boolean hasNext = false;
        String pagingState = null;
        int remainingPageSize = pageable.getPageSize();

        for (Slice<Tutorial> slice : slices) {
            if (slice.hasContent()) {
                List<Tutorial> sliceContent = slice.getContent();
                combinedContent.addAll(sliceContent);

                // Update remaining page size
                remainingPageSize -= sliceContent.size();

                // Update hasNext based on the first slice that has content
                hasNext = slice.hasNext();

                // Get paging state from the first slice that has content
                pagingState = ((CassandraPageRequest) slice.getPageable()).getPagingState().toString();

                // Break if we have enough content or no more results needed
                if (remainingPageSize <= 0) {
                    break;
                }
            }
        }

        // Adjust combined content to match requested page size
        List<Tutorial> contentForPage = combinedContent.subList(0, Math.min(pageable.getPageSize(), combinedContent.size()));

        // Create a new slice with combined content and the paging state from the first slice
        return new CassandraSliceImpl<>(contentForPage, pageable, hasNext, pagingState);
    }
}
