package techit.gongsimchae.global.security.jwt;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import techit.gongsimchae.domain.common.user.entity.UserRole;
import techit.gongsimchae.dummy.DummyObject;
import techit.gongsimchae.global.dto.AccountDto;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class JwtAuthenticationFilterTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JwtProcess jwtProcess;




    @Test
    void successfulAuthentication_test() throws Exception {
        // given
        AccountDto accountDto = new AccountDto();
        accountDto.setLoginId("test");
        accountDto.setPassword("test");
        accountDto.setRole(UserRole.ROLE_USER);
        String jwt = jwtProcess.createJwt(accountDto, JwtVO.ACCESS_CATEGORY);
        System.out.println("jwt = " + jwt);
        Cookie cookie = new Cookie(JwtVO.ACCESS_HEADER, jwt);
        ResultActions resultActions = mvc.perform(post("/user/s/hello").cookie(cookie)).andExpect(status().isNotFound());

        resultActions.andDo(print());


        // when


        // then
    }

}