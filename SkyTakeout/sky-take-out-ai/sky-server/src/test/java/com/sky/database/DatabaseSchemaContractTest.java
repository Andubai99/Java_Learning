package com.sky.database;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseSchemaContractTest {
    private static final Path SCHEMA = Path.of("src/main/resources/db/schema.sql");
    private static final Path DATA = Path.of("src/main/resources/db/data.sql");

    @Test
    void schemaDefinesAllPlanTables() throws Exception {
        String ddl = Files.readString(SCHEMA).toLowerCase();
        List<String> tables = List.of(
                "employee",
                "category",
                "dish",
                "dish_flavor",
                "setmeal",
                "setmeal_dish",
                "user",
                "address_book",
                "shopping_cart",
                "orders",
                "order_detail"
        );

        for (String table : tables) {
            assertTrue(ddl.contains("create table if not exists " + table), "missing table: " + table);
        }
    }

    @Test
    void schemaContainsCommonAuditFieldsAndSeedData() throws Exception {
        String ddl = Files.readString(SCHEMA).toLowerCase();
        String data = Files.readString(DATA).toLowerCase();

        for (String column : List.of("create_time", "update_time", "create_user", "update_user")) {
            assertTrue(ddl.contains(column), "missing common field: " + column);
        }
        assertTrue(data.contains("insert into employee"), "missing employee seed data");
        assertTrue(data.contains("admin"), "missing admin demo account");
    }

    @Test
    void schemaAvoidsStandaloneConditionalIndexStatementsForMysqlCompatibility() throws Exception {
        String ddl = Files.readString(SCHEMA).toLowerCase();

        assertFalse(ddl.contains("create index if not exists"), "MySQL does not support create index if not exists");
        for (String index : List.of(
                "idx_category_type",
                "idx_dish_category",
                "idx_setmeal_category",
                "idx_address_user",
                "idx_cart_user",
                "idx_orders_user",
                "idx_orders_status",
                "idx_order_detail_order"
        )) {
            assertTrue(ddl.contains(index), "missing index: " + index);
        }
    }
}
