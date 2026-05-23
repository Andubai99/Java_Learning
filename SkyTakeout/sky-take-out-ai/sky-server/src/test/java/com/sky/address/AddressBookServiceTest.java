package com.sky.address;

import com.sky.store.InMemorySkyStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressBookServiceTest {

    @Test
    void settingDefaultAddressClearsPreviousDefaultForSameUser() {
        AddressBookService service = new AddressBookService(new InMemorySkyStore());

        AddressBook home = service.save(4L, new AddressCommand("张三", "13800000000", "上海市", "浦东新区", "世纪大道 1 号"));
        AddressBook office = service.save(4L, new AddressCommand("张三", "13800000000", "上海市", "徐汇区", "漕溪北路 2 号"));

        service.setDefault(4L, home.getId());
        service.setDefault(4L, office.getId());

        assertEquals(0, service.findById(home.getId()).getIsDefault());
        assertEquals(1, service.findById(office.getId()).getIsDefault());
    }
}
