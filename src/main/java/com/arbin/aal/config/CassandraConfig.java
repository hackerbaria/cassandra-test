package com.arbin.aal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.AbstractSessionConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CassandraConfig extends AbstractSessionConfiguration {

    public static final String KEYSPACE = "bezkoder";


    @Override
    protected String getLocalDataCenter() {
        return "datacenter1";
    }

    @Override
    protected String getKeyspaceName() {
        return KEYSPACE;
    }

}
