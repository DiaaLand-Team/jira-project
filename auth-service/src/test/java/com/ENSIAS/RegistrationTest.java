// package com.ENSIAS;

// import com.ENSIAS.model.RegistrationRequest;
// import com.ENSIAS.service.*;
// import com.ENSIAS.controller.*;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import static org.mockito.Mockito.*;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.ArgumentCaptor;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;
// import org.junit.jupiter.api.Assertions;
// import static org.junit.Assert.assertEquals;
// import org.springframework.test.web.servlet.MvcResult;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import java.util.Optional;
// import com.ENSIAS.model.ENSIASt;
// import static org.junit.Assert.assertTrue;

// @SpringBootTest(classes = RegistrationServiceApplication.class)
// public class RegistrationTest {
//     private MockMvc mockMvc;

//     @Mock
//     private ENSIAStService ensiaStService;

//     @InjectMocks
//     private ENSIAStController controller;

//     @BeforeEach
//     public void setUp() {
//         mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//     }

//     @Test
//     public void testRegisterENSIASt() throws Exception {
//         // Create a registration request object
//         RegistrationRequest request = new RegistrationRequest();
//         request.setFirstName("John");
//         request.setLastName("Doe");
//         request.setEmail("johndoe@example.com");
//         request.setPromo(2023);
//         request.setField("Computer Science");
//         request.setPassword("password");

//         // Make the request to the server
//         MvcResult result = mockMvc.perform(post("/ENSIASts/signup")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(new ObjectMapper().writeValueAsString(request)))
//                 .andExpect(status().isCreated())
//                 .andReturn();

//         // Verify the response
//         String expectedResponse = "ENSIASt registered successfully";
//         String actualResponse = result.getResponse().getContentAsString();
//         assertEquals(expectedResponse, actualResponse);

//         // Verify that the ENSIASt object was saved to the database
//         Optional<ENSIASt> ensiaSt = ensiaStService.findByEmail(request.getEmail());
//         assertTrue(ensiaSt.isPresent());
//         assertEquals(request.getFirstName(), ensiaSt.get().getFirstName());
//         assertEquals(request.getLastName(), ensiaSt.get().getLastName());
//         assertEquals(request.getEmail(), ensiaSt.get().getEmail());
//         assertEquals(request.getPromo(), ensiaSt.get().getPromo());
//         assertEquals(request.getField(), ensiaSt.get().getField());
//         assertEquals(request.getPassword(), ensiaSt.get().getPassword());
//     }

//     // A utility method to convert a Java object into JSON string
//     private static String asJsonString(final Object obj) {
//         try {
//             return new ObjectMapper().writeValueAsString(obj);
//         } catch (Exception e) {
//             throw new RuntimeException(e);
//         }
//     }

// }
