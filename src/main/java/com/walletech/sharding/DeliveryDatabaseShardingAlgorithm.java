package com.walletech.sharding;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.LinkedHashSet;

public class DeliveryDatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Integer> {

    private Integer datasouceCount = 2;

    private Integer tableCount = 10;

    @Override
    public String doEqualSharding(Collection<String> datasourceNames, ShardingValue<Integer> shardingValue) {
        if (shardingValue.getValue() == -1){
            for (String datasourceName : datasourceNames){
                if (datasourceName.endsWith("0"))
                    return datasourceName;
            }
        }
        for (String datasourceName : datasourceNames){
            if (datasourceName.endsWith((shardingValue.getValue()/tableCount % datasouceCount)   +"")) {
                return datasourceName;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> datasourceNames, ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<String>(datasourceNames.size());
        for (Integer value : shardingValue.getValues()) {
            for (String datasourceName : datasourceNames) {
                if (datasourceName.endsWith(value /tableCount % datasouceCount  + "")) {
                    result.add(datasourceName);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> datasourceNames, ShardingValue<Integer> shardingValue) {
        Collection<String> result = new LinkedHashSet<String>(datasourceNames.size());
        Range<Integer> range = (Range<Integer>) shardingValue.getValueRange();
        for (Integer i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String datasourceName : datasourceNames) {
                if (datasourceName.endsWith(i /tableCount % datasouceCount  + "")) {
                    result.add(datasourceName);
                }
            }
        }
        return result;
    }
}
