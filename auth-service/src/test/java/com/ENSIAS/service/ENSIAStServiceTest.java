package com.ENSIAS.service;

import com.ENSIAS.model.*;
import com.ENSIAS.repository.EnsiastRepository;
import com.ENSIAS.repository.TokenRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.ENSIAS.enums.Role;
import com.ENSIAS.enums.State;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedMetric;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.mockito.MockitoAnnotations;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;


//@AllArgsConstructor
@ExtendWith(MockitoExtension.class)
class ENSIAStServiceTest {
    @Mock
    private EnsiastRepository ensiastRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private ENSIAStService ensiaStService ;

    @BeforeEach
    void setUp() {
        //ensiastRepository = mock(EnsiastRepository.class) ;
//        tokenRepository = mock(TokenRepository.class) ;
        //bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class) ;
//        jwtService = mock(JwtService.class) ;
//        authenticationManager = mock(AuthenticationManager.class) ;

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void registerENSIAStShouldReturnNull() {

        // given
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("test@example.com");
        when(ensiaStService.findByEmail("test@example.com")).thenReturn(Optional.of(new ENSIASt()));

        // when
        ENSIASt result = ensiaStService.registerENSIASt(request);

        // then
        assertNull(result);
        verify(ensiastRepository, never()).saveAndFlush(any(ENSIASt.class));

    }

    @Test
    void registerENSIAStShouldSaveNewENSIASt(){

        RegistrationRequest request = new RegistrationRequest();

        request.setEmail("test@elmrabti.com");
        request.setFirstName("Hamza");
        request.setLastName("Elmrabti");
        request.setPassword("password");
        request.setPromo(2025);
        request.setField("Computer Science");
        when(ensiaStService.findByEmail("test@elmrabti.com")).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encoded_password");

        // when
        ENSIASt result = ensiaStService.registerENSIASt(request);

        // then
        assertNotNull(result);
        assertEquals("Hamza", result.getFirstName());
        assertEquals("Elmrabti", result.getLastName());
        assertEquals("test@elmrabti.com", result.getEmail());
        assertEquals(2025, result.getPromo());
        assertEquals("Computer Science", result.getField());
        assertEquals("encoded_password", result.getPassword());
        assertEquals(State.INACTIF, result.getState());
        assertEquals(Role.USER, result.getRole());
        verify(ensiastRepository, times(1)).saveAndFlush(result);

    }



    @Test
    void checkRegistrationShouldReturnConflict() {
        // given
        ENSIASt existingENSIASt = null;

        // when
        ResponseEntity<String> response = ensiaStService.checkRegistration(existingENSIASt);

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("This email is already registered", response.getBody());
    }


    @Test
    void checkRegistrationShouldReturnOk() {
        // given
        ENSIASt nonExistingENSIASt = new ENSIASt();

        // when
        ResponseEntity<String> response = ensiaStService.checkRegistration(nonExistingENSIASt);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ENSIASt created", response.getBody());
    }



    @Test
    void loginShouldReturnAuthResponseWithAccessTokenWhenEmailAndPasswordAreCorrect() {
        // given
        String email = "test@elmrabti.com";
        String password = "password";
        ENSIASt ensiaSt = new ENSIASt();
        ensiaSt.setEmail(email);
        ensiaSt.setPassword(password);
        when(ensiastRepository.findByEmail(email)).thenReturn(Optional.of(ensiaSt));

        // Mock the authenticationManager.authenticate() method call
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(jwtService.generateToken(any())).thenReturn("ABCDF") ;

        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        AuthResponse result = ensiaStService.login(request);

        // then
        assertNotNull(result);
        assertNotNull(result.getAccessToken());
        verify(ensiastRepository, times(1)).findByEmail(email);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));

    }



    @Test
    void loginShouldNotReturnAuthResponseWhenPasswordNotCorrect() {
        // given
        String email = "test@elmrabti.com";
        String password = "password";
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        //Ensiast in database with other password
        ENSIASt ensiaSt = new ENSIASt() ;
        ensiaSt.setEmail(email);
        String pass = "pass" ;
        ensiaSt.setPassword(pass);

        when(ensiastRepository.findByEmail(email)).thenReturn(Optional.of(ensiaSt));

        // when
        AuthResponse result = ensiaStService.login(request);

        // then
        assertNotNull(result);
        assertNull(result.getAccessToken());
        verify(ensiastRepository, times(1)).findByEmail(email);
    }




    @Test
    void findAll() {

        ENSIASt ensiaSt1 = new ENSIASt();
        ENSIASt ensiaSt2 = new ENSIASt();
        ENSIASt ensiaSt3 = new ENSIASt();

        ensiaSt1.setFirstName("Hamza");
        ensiaSt1.setLastName("Elmrabti");
        ensiaSt1.setEmail("hamza@elmrabti.com");

        ensiaSt2.setFirstName("Yassine");
        ensiaSt2.setLastName("Benabdellah");
        ensiaSt2.setEmail("yassine@gmail.com");

        ensiaSt2.setFirstName("Zineb");
        ensiaSt2.setLastName("Cherkaoui");
        ensiaSt2.setEmail("zineb@gmail.com");

        List<ENSIASt> ensiaStList = Arrays.asList(ensiaSt1, ensiaSt2, ensiaSt3);
        //When
        when(ensiastRepository.findAll()).thenReturn(ensiaStList) ;
        //Call
        List<ENSIASt> result = ensiaStService.findAll();

        //Verify
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
        assertEquals(ensiaStList, result);
        verify(ensiastRepository, times(1)).findAll();


    }

    //@Test
    //void findByEmail() {
    //}

    @Test
    void findByLastName() {
        String lastName = "Elmrabti" ;

        ENSIASt ensiaSt1 = new ENSIASt();
        ENSIASt ensiaSt2 = new ENSIASt();

        ensiaSt1.setFirstName("Hamza");
        ensiaSt1.setLastName(lastName);
        ensiaSt1.setEmail("hamza@elmrabti.com");

        ensiaSt2.setFirstName("Yassine");
        ensiaSt2.setLastName(lastName);
        ensiaSt2.setEmail("yassine@gmail.com");

        List<ENSIASt> ensiaStList = Arrays.asList(ensiaSt1, ensiaSt2);
        //When
        when(ensiastRepository.findByLastName(lastName)).thenReturn(Optional.of(ensiaStList)) ;
        //Call
        Optional<List<ENSIASt>> result = ensiaStService.findByLastName(lastName);

        //Verify
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        assertEquals(ensiaStList, result.get());
        verify(ensiastRepository, times(1)).findByLastName(lastName);


    }

    @Test
    void findByPromo() {

        int promo =2024 ;
        ENSIASt ensiaSt1 = new ENSIASt();
        ENSIASt ensiaSt2 = new ENSIASt();

        ensiaSt1.setFirstName("Hamza");
        ensiaSt1.setLastName("Elmrabti");
        ensiaSt1.setPromo(promo);
        ensiaSt1.setEmail("hamza@elmrabti.com");

        ensiaSt2.setFirstName("Yassine");
        ensiaSt2.setLastName("Alami");
        ensiaSt2.setPromo(promo);
        ensiaSt2.setEmail("yassine@gmail.com");

        List<ENSIASt> ensiaStList = Arrays.asList(ensiaSt1, ensiaSt2);
        //When
        when(ensiastRepository.findByPromo(promo)).thenReturn(Optional.of(ensiaStList)) ;
        //Call
        Optional<List<ENSIASt>> result = ensiaStService.findByPromo(promo);

        //Verify
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        assertEquals(ensiaStList, result.get());
        verify(ensiastRepository, times(1)).findByPromo(promo);




    }


    @Test
    void testFindByPromoAndField() {
        // Test data
        int promo =2024 ;
        String field = "Computer Science" ;
        ENSIASt ensiaSt1 = new ENSIASt();
        ENSIASt ensiaSt2 = new ENSIASt();

        ensiaSt1.setFirstName("Hamza");
        ensiaSt1.setLastName("Elmrabti");
        ensiaSt1.setPromo(promo);
        ensiaSt1.setEmail("hamza@elmrabti.com");
        ensiaSt1.setField(field);
        ensiaSt1.setPassword("password");

        ensiaSt2.setFirstName("Yassine");
        ensiaSt2.setLastName("Alami");
        ensiaSt2.setPromo(promo);
        ensiaSt2.setEmail("yassine@gmail.com");
        ensiaSt2.setField(field);
        ensiaSt2.setPassword("password");

        List<ENSIASt> ensiaStList = Arrays.asList(ensiaSt1, ensiaSt2);

        when(ensiastRepository.findByPromoAndField(promo, field)).thenReturn(Optional.of(ensiaStList)) ;

        //Call
        Optional<List<ENSIASt>> result = ensiaStService.findByPromoAndField(promo, field);

        //Verify
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        assertEquals(ensiaStList, result.get());
        verify(ensiastRepository, times(1)).findByPromoAndField(promo, field);

    }




    @Test
    public void testFindByField() {
        String field = "Computer Science";
        ENSIASt ensiaSt1 = new ENSIASt();
        ENSIASt ensiaSt2 = new ENSIASt();

        ensiaSt1.setFirstName("Hamza");
        ensiaSt1.setLastName("Elmrabti");
        ensiaSt1.setPromo(2024);
        ensiaSt1.setEmail("hamza@elmrabti.com");
        ensiaSt1.setField(field);

        ensiaSt2.setFirstName("Yassine");
        ensiaSt2.setLastName("Alami");
        ensiaSt2.setPromo(2025);
        ensiaSt2.setEmail("yassine@gmail.com");
        ensiaSt2.setField(field);

        List<ENSIASt> ensiaStList = Arrays.asList(ensiaSt1, ensiaSt2);

        //When
        when(ensiastRepository.findByField(field)).thenReturn(Optional.of(ensiaStList));

        //Call the service
        Optional<List<ENSIASt>> result = ensiaStService.findByField(field);

        //Verify
        assertTrue(result.isPresent());
        assertEquals(ensiaStList, result.get());
        verify(ensiastRepository, times(1)).findByField(field);

    }


    @Test
    void findActiveENSIAStsShouldReturnActiveENSIASts() {
        // given
        List<ENSIASt> activeENSIASts = new ArrayList<>();
        ENSIASt ensiaSt1 = new ENSIASt() ;
        ENSIASt ensiaSt2 = new ENSIASt() ;

        ensiaSt1.setFirstName("Hamza");
        ensiaSt1.setLastName("Elmrabti");
        ensiaSt1.setPromo(2024);
        ensiaSt1.setEmail("hamza@elmrabti.com");
        ensiaSt1.setPassword("password");
        ensiaSt1.setField("Computer Science");
        ensiaSt1.setState(State.ACTIF);

        ensiaSt2.setFirstName("Yassine");
        ensiaSt2.setLastName("Alami");
        ensiaSt2.setPromo(2025);
        ensiaSt2.setEmail("yassine@gmail.com");
        ensiaSt2.setPassword("password");
        ensiaSt2.setField("Computer Science");
        ensiaSt2.setState(State.ACTIF);

        activeENSIASts.add(ensiaSt1) ;
        activeENSIASts.add(ensiaSt2) ;

        when(ensiastRepository.findByState(State.ACTIF)).thenReturn(Optional.of(activeENSIASts));

        Optional<List<ENSIASt>> result = ensiaStService.findActiveENSIASts();

        // then
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        assertEquals("hamza@elmrabti.com", result.get().get(0).getEmail());
        assertEquals("yassine@gmail.com", result.get().get(1).getEmail());
        verify(ensiastRepository, times(1)).findByState(State.ACTIF);
    }



    @Test
    void addRoleShouldUpdateRoleToAdmin() {
        ENSIASt ensiaSt = new ENSIASt();
        ensiaSt.setEmail("test@elmrabti.com");
        ensiaSt.setRole(Role.USER);

        when(ensiastRepository.findByEmail("test@elmrabti.com")).thenReturn(Optional.of(ensiaSt));

        ensiaStService.addRole("test@elmrabti.com");

        verify(ensiastRepository, times(1)).saveAndFlush(argThat(updatedENSIASt ->
                updatedENSIASt.getEmail().equals("test@elmrabti.com")
                        && updatedENSIASt.getRole() == Role.ADMIN));
    }


}