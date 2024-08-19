package techit.gongsimchae.domain.common.address.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.address.dto.AddressCreateReqDtoWeb;
import techit.gongsimchae.domain.common.address.dto.AddressRespDtoWeb;
import techit.gongsimchae.domain.common.address.dto.AddressUpdateReqDtoWeb;
import techit.gongsimchae.domain.common.address.entity.Address;
import techit.gongsimchae.domain.common.address.entity.DefaultAddressStatus;
import techit.gongsimchae.domain.common.address.repository.AddressRepository;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public List<AddressRespDtoWeb> getAddresses(PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));
        return addressRepository.findAllByUser(user.getId()).stream().map(AddressRespDtoWeb::new).collect(Collectors.toList());
    }

    @Transactional
    public void addAddress(AddressCreateReqDtoWeb addressCreateReqDtoWeb, PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));
        addressCreateReqDtoWeb.applySetting(user);
        unsetAsDefault();
        Address address = new Address(addressCreateReqDtoWeb, user, DefaultAddressStatus.PRIMARY);

        addressRepository.save(address);
    }

    public AddressRespDtoWeb getAddress(String id) {
        Address address = addressRepository.findByUID(id).orElseThrow(() -> new CustomWebException(ErrorMessage.ADDRESS_NOT_FOUND));
        return new AddressRespDtoWeb(address);
    }

    @Transactional
    public void updateAddress(String id, AddressUpdateReqDtoWeb addressUpdateReqDtoWeb) {
        Address address = addressRepository.findByUID(id).orElseThrow(() -> new CustomWebException(ErrorMessage.ADDRESS_NOT_FOUND));
        address.changeInfo(addressUpdateReqDtoWeb);
    }

    @Transactional
    public void deleteAddress(String id) {
        Address address = addressRepository.findByUID(id).orElseThrow(() -> new CustomWebException(ErrorMessage.ADDRESS_NOT_FOUND));
        addressRepository.delete(address);
    }

    @Transactional
    public void changeDefaultAddress(String id) {
        unsetAsDefault();
        Address address = addressRepository.findByUID(id).orElseThrow(() -> new CustomWebException(ErrorMessage.ADDRESS_NOT_FOUND));
        address.setDefaultAddress();
    }

    private void unsetAsDefault() {
        Optional<Address> defaultAddress = addressRepository.findDefaultAddress();
        if (defaultAddress.isPresent()) {
            Address address = defaultAddress.get();
            address.unsetDefaultAddress();
        }
    }
}
