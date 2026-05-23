package com.sky.address;

import com.sky.common.BusinessException;
import com.sky.store.InMemorySkyStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AddressBookService {
    private final InMemorySkyStore store;

    public AddressBookService(InMemorySkyStore store) {
        this.store = store;
    }

    public AddressBook save(Long userId, AddressCommand command) {
        AddressBook address = new AddressBook();
        address.setId(store.nextId());
        address.setUserId(userId);
        fill(address, command);
        store.addressBooks().add(address);
        return address;
    }

    public AddressBook update(Long userId, Long id, AddressCommand command) {
        AddressBook address = findOwned(userId, id);
        fill(address, command);
        return address;
    }

    public List<AddressBook> list(Long userId) {
        return store.addressBooks().stream()
                .filter(address -> Objects.equals(address.getUserId(), userId))
                .toList();
    }

    public AddressBook findById(Long id) {
        return store.addressBooks().stream()
                .filter(address -> Objects.equals(address.getId(), id))
                .findFirst()
                .orElseThrow(() -> new BusinessException("地址不存在"));
    }

    public AddressBook defaultAddress(Long userId) {
        return list(userId).stream()
                .filter(address -> address.getIsDefault() == 1)
                .findFirst()
                .orElse(null);
    }

    public void setDefault(Long userId, Long id) {
        AddressBook target = findOwned(userId, id);
        list(userId).forEach(address -> address.setIsDefault(0));
        target.setIsDefault(1);
    }

    public void delete(Long userId, Long id) {
        findOwned(userId, id);
        store.removeAddressBook(id);
    }

    private AddressBook findOwned(Long userId, Long id) {
        AddressBook address = findById(id);
        if (!Objects.equals(address.getUserId(), userId)) {
            throw new BusinessException("地址不存在");
        }
        return address;
    }

    private static void fill(AddressBook address, AddressCommand command) {
        address.setConsignee(command.consignee());
        address.setPhone(command.phone());
        address.setProvinceName(command.provinceName());
        address.setCityName(command.cityName());
        address.setDetail(command.detail());
    }
}
