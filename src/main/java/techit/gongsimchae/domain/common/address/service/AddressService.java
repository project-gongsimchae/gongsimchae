package techit.gongsimchae.domain.common.address.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.address.dto.AddressCreateReqDtoWeb;
import techit.gongsimchae.domain.common.address.dto.AddressRespDtoWeb;
import techit.gongsimchae.domain.common.address.dto.AddressUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.address.entity.Address;
import techit.gongsimchae.domain.common.address.repository.AddressRepository;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public List<AddressRespDtoWeb> getAddresses(PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found User"));
        return addressRepository.findAllByUser(user.getId()).stream().map(AddressRespDtoWeb::new).collect(Collectors.toList());
    }

    public void addAddress(AddressCreateReqDtoWeb addressCreateReqDtoWeb, PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException("not found User"));
        addressCreateReqDtoWeb.setReceiver(user.getName());
        addressCreateReqDtoWeb.setPhoneNumber(user.getPhoneNumber());
        Address address = new Address(addressCreateReqDtoWeb, user);
        addressRepository.save(address);
    }

    public AddressRespDtoWeb getAddress(String id) {
        Address address = addressRepository.findByUID(id).orElseThrow(() -> new CustomWebException("not found Address"));
        return new AddressRespDtoWeb(address);
    }
    @Transactional
    public void updateAddress(String id, AddressUpdateReqDtoWeb addressUpdateReqDtoWeb) {
        Address address = addressRepository.findByUID(id).orElseThrow(() -> new CustomWebException("not found Address"));
        address.changeInfo(addressUpdateReqDtoWeb);
    }
    @Transactional
    public void deleteAddress(String id) {
        Address address = addressRepository.findByUID(id).orElseThrow(() -> new CustomWebException("not found Address"));
        addressRepository.delete(address);
    }
}
