package name.junnikym.consumptionHistory.history.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import name.junnikym.consumptionHistory.exception.AlreadyExistsException;
import name.junnikym.consumptionHistory.history.domain.History;
import name.junnikym.consumptionHistory.history.dto.HistoryCreateDTO;
import name.junnikym.consumptionHistory.history.dto.HistorySummaryDTO;
import name.junnikym.consumptionHistory.history.dto.HistoryUpdateDTO;
import name.junnikym.consumptionHistory.history.service.HistoryService;
import name.junnikym.consumptionHistory.member.domain.Member;
import name.junnikym.consumptionHistory.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@Transactional
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("???????????? API ?????????")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HistoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ObjectMapper objectMapper;

	final String apiVersionUrl = "/api/v1";

	/**
	 * Test Member ??????
	 */

	@Autowired
	MemberRepository memberRepository;

	final String testMemberEmail = "test_on_junit_main@test";
	final String testMemberEmailSub = "test_on_junit_sub@test";
	final String testMemberPassword = "password";

	private Member testMember;
	private Member testMemberSub;

	@BeforeAll
	public void testMemberSetup() {

		// Main Member Creation
		if (memberRepository.existsByEmail(testMemberEmail))
			throw new AlreadyExistsException("Email");

		testMember = memberRepository.save(
				Member.builder()
						.email(testMemberEmail)
						.password(testMemberPassword)
						.build()
		);

		// Sub Member Creation
		if (memberRepository.existsByEmail(testMemberEmailSub))
			throw new AlreadyExistsException("Email");

		testMemberSub = memberRepository.save(
				Member.builder()
						.email(testMemberEmailSub)
						.password(testMemberPassword)
						.build()
		);
	}

	@AfterAll
	public void testMemberCleanup() {
		memberRepository.delete(testMember);
		memberRepository.delete(testMemberSub);
	}

	private MockHttpServletRequestBuilder creationRequest(
			Long amount,
			String summaryMemo,
			String detailMemo
	) throws Exception {

		return post(apiVersionUrl+"/history")
				.contentType( MediaType.APPLICATION_JSON_VALUE )
				.content( objectMapper.writeValueAsString(
						HistoryCreateDTO.builder()
								.amount(amount)
								.summaryMemo(summaryMemo)
								.detailMemo(detailMemo)
								.build()
				));
	}





	/**
	 * ???????????? ?????? ?????????
	 */
	@Nested
	@DisplayName("???????????? ?????? ?????????")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class HistoryCreationTest {

		final Integer amount = 1000;
		final String summaryMemo = "History creation test - JUnit";
		final String detailMemo = "History creation test on JUnit";

		/**
		 * ???????????? ?????? ?????? ??????????????? ???????????? ?????? ??????
		 */
		@Test
		@DisplayName("???????????? ?????? - ???????????? ???")
		void createHistoryWhenFailAuth () throws Exception {

			MockHttpServletRequestBuilder request =
					creationRequest(Long.valueOf(amount), summaryMemo, detailMemo);

			mockMvc.perform(request)
					.andExpect(status().isUnauthorized());
		}

		/**
		 * ??????????????? ????????? ??? ??????????????? ????????? ??????
		 */
		@Test
		@DisplayName("???????????? ?????? - ????????? ???")
		@WithUserDetails(
				value = testMemberEmail,
				userDetailsServiceBeanName = "memberService"
		)
		void createHistory () throws Exception {

			MockHttpServletRequestBuilder request =
					creationRequest(Long.valueOf(amount), summaryMemo, detailMemo);

			mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json"))
					.andExpect(jsonPath("$.amount", is(amount)))
					.andExpect(jsonPath("$.summaryMemo", is(summaryMemo)))
					.andExpect(jsonPath("$.detailMemo", is(detailMemo)))
					.andExpect(jsonPath("$.isDeleted", is(false)));
		}
	}






	/**
	 * History ?????? ?????? ??? ObjectMapper??? ?????? id??? ???????????? ?????? ????????? ?????????
	 */
	static class HistoryTestIdHooker {
		public UUID id;
	}

	/**
	 * ???????????? ?????? History ????????? ????????? ??????
	 * 	-> ????????? ?????? ??? 3?????? History ?????? ?????? ??? ????????? ??????
	 */
	@Nested
	@DisplayName("???????????? ?????? ??? ????????? ?????????")
	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	class AfterHistoryCreate {

		final Long amount = 1000L;
		final String summaryMemo = "History selection test - JUnit (";
		final String detailMemo = "History selection test on JUnit";

		private List<History> histories;

		@BeforeAll
		void testHistorySetup() throws Exception {
			histories = new ArrayList<>();

			for (Integer i = 1; i <= 3; i++) {
				try {
					histories.add(
							historyService.createHistory(
									testMember,
									HistoryCreateDTO.builder()
											.amount(amount)
											.summaryMemo(summaryMemo + i.toString() + ")")
											.detailMemo(detailMemo)
											.build()
							)
					);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@AfterAll
		void testHistoryCleanup() throws Exception {
			historyService.fullyDeleteHistoryList (
					testMember,
					histories.stream().map(x->x.getId()).collect(Collectors.toList())
			);
		}

		/**
		 * ??????????????? ????????? ????????? ????????? ??????
		 */
		@Nested
		@DisplayName("???????????? ?????? ?????????")
		class HistorySelect {

			/**
			 * ????????? ???????????? ????????? ??????????????? ????????? ????????? ????????? ???????????? ??????
			 */
			@Test
			@DisplayName("????????????(??????) ????????? ?????? - ????????? ???")
			@WithUserDetails(
					value = testMemberEmail,
					userDetailsServiceBeanName = "memberService"
			)
			void getOwnHistoryList () throws Exception {
				String id = histories.get(0).getId().toString();

				String json = mockMvc.perform(
								get(apiVersionUrl+"/history")
										.accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
						)
						.andExpect(status().isOk())
						.andReturn().getResponse().getContentAsString();

				Map<UUID, HistorySummaryDTO> result =
						objectMapper.readValue(json, new TypeReference<List<HistorySummaryDTO>>(){})
								.stream()
								.collect(Collectors.toMap(x->x.getId(), x->x));

				// ????????? History ????????? Select ??? ?????? ???????????? ???????????? ??????
				// 	-> ???????????? ?????? ??? ??????
				for(History it : histories) {
					HistorySummaryDTO target = result.get(it.getId());
					assertNotNull(target);
					assertEquals(it.getAmount(), target.getAmount());
					assertEquals(it.getSummaryMemo(), target.getSummaryMemo());
				}

			}

			/**
			 * ???????????? ?????? ?????? ????????????(??????) ???????????? ???????????? ??????
			 */
			@Test
			@DisplayName("????????????(??????) ????????? ?????? - ???????????? ???")
			void getOwnHistoryListWhenFailAuth () throws Exception {

				String id = histories.get(0).getId().toString();

				mockMvc.perform(
						get(apiVersionUrl+"/history")
				)
				.andExpect(status().isUnauthorized());
			}

			/**
			 * ???????????? ????????? ???????????? ??? ???????????? ????????? ???????????? ??????
			 */
			@Test
			@DisplayName("????????????(?????????) ?????? - ????????? ????????? ???")
			@WithUserDetails(
					value = testMemberEmail,
					userDetailsServiceBeanName = "memberService"
			)
			void getOwnHistoryDetail () throws Exception {

				History history = histories.get(0);

				mockMvc.perform(
						get(apiVersionUrl+"/history/"+history.getId().toString())
								.accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.amount", is(history.getAmount().intValue())))
				.andExpect(jsonPath("$.summaryMemo", is(history.getSummaryMemo())))
				.andExpect(jsonPath("$.detailMemo", is(history.getDetailMemo())));
			}

			/**
			 * ????????? ???????????? ?????? ???????????? ????????????(?????????)??? ???????????? ??????
			 */
			@Test
			@DisplayName("????????????(?????????) ?????? - ?????? ????????? ????????? ???")
			@WithUserDetails(
					value = testMemberEmailSub,
					userDetailsServiceBeanName = "memberService"
			)
			void getOwnHistoryDetailWhenOtherWriter () throws Exception {

				History history = histories.get(0);

				mockMvc.perform(
						get(apiVersionUrl+"/history/"+history.getId().toString())
				)
				.andDo(print())
				.andExpect(status().isNotFound());
			}

			/**
			 * ???????????? ???????????? ????????????(?????????)??? ?????? ?????? ??????
			 */
			@Test
			@DisplayName("????????????(?????????) ?????? - ???????????? ???")
			void getOwnHistoryDetailWhenFailAuth () throws Exception {

				History history = histories.get(0);

				mockMvc.perform(
						get(apiVersionUrl+"/history/"+history.getId().toString())
				)
				.andDo(print())
				.andExpect(status().isUnauthorized());
			}
		}



		/**
		 * History ????????? ????????? ???????????? ??????
		 */
		@Nested
		@DisplayName("???????????? ?????? ?????????")
		class HistoryUpdate {

			// 0?????? ????????? ?????? ????????? ??????
			final Integer updatedAmount = 9000;
			final String updatedSummaryMemo = "History update test - JUnit (?)";
			final String updatedDetailMemo = "History update test on JUnit";

			private MockHttpServletRequestBuilder updateRequest (String id) throws Exception {
				return put(apiVersionUrl + "/history/" + id)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(objectMapper.writeValueAsString(
								HistoryUpdateDTO.builder()
										.amount(Long.valueOf(updatedAmount))
										.summaryMemo(updatedSummaryMemo)
										.detailMemo(updatedDetailMemo)
										.build()
						));
			}

			@Test
			@DisplayName("???????????? ?????? - ????????? ?????? / ????????? ???")
			@WithUserDetails(
					value = testMemberEmail,
					userDetailsServiceBeanName = "memberService"
			)
			void updateHistory () throws Exception {

				mockMvc.perform( updateRequest(histories.get(0).getId().toString()))
						.andExpect(status().isOk())
						.andExpect(content().contentType("application/json"))
						.andExpect(jsonPath("$.amount", is(updatedAmount)))
						.andExpect(jsonPath("$.summaryMemo", is(updatedSummaryMemo)))
						.andExpect(jsonPath("$.detailMemo", is(updatedDetailMemo)));
			}

			@Test
			@DisplayName("???????????? ?????? - ????????? ?????? / ???????????? ???")
			void updateHistoryWithFailAuth () throws Exception {

				mockMvc.perform( updateRequest(histories.get(0).getId().toString()))
						.andExpect(status().isUnauthorized());
			}

			@Test
			@DisplayName("???????????? ?????? - ????????? ?????? / ???????????? ????????? ???")
			@WithUserDetails(
					value = testMemberEmailSub,
					userDetailsServiceBeanName = "memberService"
			)
			void updateHistoryWithOtherWriter () throws Exception {

				mockMvc.perform( updateRequest(histories.get(0).getId().toString()))
						.andExpect(status().isNotFound());
			}

			@Test
			@DisplayName("???????????? ?????? - ???????????? ?????? ?????? / ????????? ???")
			@WithUserDetails(
					value = testMemberEmail,
					userDetailsServiceBeanName = "memberService"
			)
			void updateHistoryWithNotExist () throws Exception {

				mockMvc.perform( updateRequest("00000000-0000-0000-0000-000000000000"))
						.andExpect(status().isNotFound());
			}
		}



		/**
		 * History ????????? ????????? ???????????? ??????
		 */
		@Nested
		@DisplayName("???????????? ?????? ?????????")
		class HistoryDelete {

			@Nested
			@DisplayName("???????????? ????????? ??????")
			class HistoryDeleteSingle {

				@Test
				@DisplayName("???????????? ?????? (??????) - ????????? ?????? / ????????? ???")
				@WithUserDetails(
						value = testMemberEmail,
						userDetailsServiceBeanName = "memberService"
				)
				void deleteHistory () throws Exception {

					String id = histories.get(0).getId().toString();
					mockMvc.perform(delete(apiVersionUrl + "/history/" + id))
							.andExpect(status().isOk());
				}

				@Test
				@DisplayName("???????????? ?????? (??????) - ???????????? ?????? ?????? / ????????? ???")
				@WithUserDetails(
						value = testMemberEmail,
						userDetailsServiceBeanName = "memberService"
				)
				void deleteHistoryWithNotExist () throws Exception {

					mockMvc.perform(delete(apiVersionUrl + "/history/00000000-0000-0000-0000-000000000000"))
							.andExpect(status().isNotFound());
				}

				@Test
				@DisplayName("???????????? ?????? (??????) - ????????? ?????? / ???????????? ????????? ???")
				@WithUserDetails(
						value = testMemberEmailSub,
						userDetailsServiceBeanName = "memberService"
				)
				void deleteHistoryWithOtherWriter () throws Exception {

					String id = histories.get(0).getId().toString();
					mockMvc.perform(delete(apiVersionUrl + "/history/" + id))
							.andExpect(status().isNotFound());
				}

				@Test
				@DisplayName("???????????? ?????? (??????) - ????????? ?????? / ???????????? ???")
				void deleteHistoryWithFailAuth () throws Exception {

					String id = histories.get(0).getId().toString();
					mockMvc.perform(delete(apiVersionUrl + "/history/" + id))
							.andExpect(status().isUnauthorized());
				}
			}

			@Nested
			@DisplayName("???????????? ????????? ??????")
			class HistoryDeleteMultiple {

				@Test
				@DisplayName("???????????? ?????? (?????????) - ????????? ?????? / ????????? ???")
				@WithUserDetails(
						value = testMemberEmail,
						userDetailsServiceBeanName = "memberService"
				)
				void deleteHistory () throws Exception {

					String id1 = histories.get(0).getId().toString();
					String id2 = histories.get(1).getId().toString();
					mockMvc.perform(delete(apiVersionUrl + "/history?id=" + id1 + "&id=" + id2))
							.andExpect(status().isOk());
				}

				@Test
				@DisplayName("???????????? ?????? (?????????) - ???????????? ?????? ?????? / ????????? ???")
				@WithUserDetails(
						value = testMemberEmail,
						userDetailsServiceBeanName = "memberService"
				)
				void deleteHistoryWithNotExist () throws Exception {

					String id = "00000000-0000-0000-0000-000000000000";
					mockMvc.perform(delete(apiVersionUrl + "/history?id=" + id + "&id=" + id))
							.andExpect(status().isNotFound());
				}

				@Test
				@DisplayName("???????????? ?????? (?????????) - ????????? ?????? / ???????????? ????????? ???")
				@WithUserDetails(
						value = testMemberEmailSub,
						userDetailsServiceBeanName = "memberService"
				)
				void deleteHistoryWithOtherWriter () throws Exception {

					String id1 = histories.get(0).getId().toString();
					String id2 = histories.get(1).getId().toString();
					mockMvc.perform(delete(apiVersionUrl + "/history?id=" + id1 + "&id=" + id2))
							.andExpect(status().isNotFound());
				}

				@Test
				@DisplayName("???????????? ?????? (?????????) - ????????? ?????? / ???????????? ???")
				void deleteHistoryWithFailAuth () throws Exception {

					String id1 = histories.get(0).getId().toString();
					String id2 = histories.get(1).getId().toString();
					mockMvc.perform(delete(apiVersionUrl + "/history?id=" + id1 + "&id=" + id2))
							.andExpect(status().isUnauthorized());
				}
			}
		}

		@Nested
		@DisplayName("???????????? ?????? ?????????")
		@TestInstance(TestInstance.Lifecycle.PER_CLASS)
		class HistoryRecover {

			private History deleteHistory1;
			private History deleteHistory2;

			@BeforeAll
			void setup () throws Exception {
				deleteHistory1 = histories.get(0);
				historyService.deleteOrRecoverHistory(testMember, deleteHistory1.getId(), true);
				deleteHistory2 = histories.get(1);
				historyService.deleteOrRecoverHistory(testMember, deleteHistory2.getId(), true);
			}

			@AfterAll
			void cleanup () throws Exception {
				historyService.deleteOrRecoverHistory(testMember, deleteHistory1.getId(), false);
				historyService.deleteOrRecoverHistory(testMember, deleteHistory2.getId(), false);
			}

			@Nested
			@DisplayName("???????????? ????????? ??????")
			class HistoryRecoverSingle {

				@Test
				@DisplayName("???????????? ?????? (??????) - ????????? ?????? / ????????? ???")
				@WithUserDetails(
						value = testMemberEmail,
						userDetailsServiceBeanName = "memberService"
				)
				void deleteHistory () throws Exception {

					String id = deleteHistory1.getId().toString();
					mockMvc.perform(patch(apiVersionUrl + "/history/recover/" + id))
							.andExpect(status().isOk());
				}

				@Test
				@DisplayName("???????????? ?????? (??????) - ???????????? ?????? ?????? / ????????? ???")
				@WithUserDetails(
						value = testMemberEmail,
						userDetailsServiceBeanName = "memberService"
				)
				void deleteHistoryWithNotExist () throws Exception {

					mockMvc.perform(patch(apiVersionUrl + "/history/recover/00000000-0000-0000-0000-000000000000"))
							.andExpect(status().isNotFound());
				}

				@Test
				@DisplayName("???????????? ?????? (??????) - ????????? ?????? / ???????????? ????????? ???")
				@WithUserDetails(
						value = testMemberEmailSub,
						userDetailsServiceBeanName = "memberService"
				)
				void deleteHistoryWithOtherWriter () throws Exception {

					String id = deleteHistory1.getId().toString();
					mockMvc.perform(patch(apiVersionUrl + "/history/recover/" + id))
							.andExpect(status().isNotFound());
				}

				@Test
				@DisplayName("???????????? ?????? (??????) - ????????? ?????? / ???????????? ???")
				void deleteHistoryWithFailAuth () throws Exception {

					String id = deleteHistory1.getId().toString();
					mockMvc.perform(patch(apiVersionUrl + "/history/recover/" + id))
							.andExpect(status().isUnauthorized());
				}
			}

			@Nested
			@DisplayName("???????????? ????????? ??????")
			class HistoryRecoverMultiple {

				@Test
				@DisplayName("???????????? ?????? (?????????) - ????????? ?????? / ????????? ???")
				@WithUserDetails(
						value = testMemberEmail,
						userDetailsServiceBeanName = "memberService"
				)
				void deleteHistory () throws Exception {

					String id1 = deleteHistory1.getId().toString();
					String id2 = deleteHistory2.getId().toString();
					mockMvc.perform(patch(apiVersionUrl + "/history/recover?id=" + id1 + "&id=" + id2))
							.andExpect(status().isOk());
				}

				@Test
				@DisplayName("???????????? ?????? (?????????) - ???????????? ?????? ?????? / ????????? ???")
				@WithUserDetails(
						value = testMemberEmail,
						userDetailsServiceBeanName = "memberService"
				)
				void deleteHistoryWithNotExist () throws Exception {

					String id = "00000000-0000-0000-0000-000000000000";
					mockMvc.perform(patch(apiVersionUrl + "/history/recover?id=" + id + "&id=" + id))
							.andExpect(status().isNotFound());
				}

				@Test
				@DisplayName("???????????? ?????? (?????????) - ????????? ?????? / ???????????? ????????? ???")
				@WithUserDetails(
						value = testMemberEmailSub,
						userDetailsServiceBeanName = "memberService"
				)
				void deleteHistoryWithOtherWriter () throws Exception {

					String id1 = deleteHistory1.getId().toString();
					String id2 = deleteHistory2.getId().toString();
					mockMvc.perform(patch(apiVersionUrl + "/history/recover?id=" + id1 + "&id=" + id2))
							.andExpect(status().isNotFound());
				}

				@Test
				@DisplayName("???????????? ?????? (?????????) - ????????? ?????? / ???????????? ???")
				void deleteHistoryWithFailAuth () throws Exception {

					String id1 = deleteHistory1.getId().toString();
					String id2 = deleteHistory2.getId().toString();
					mockMvc.perform(patch(apiVersionUrl + "/history/recover?id=" + id1 + "&id=" + id2))
							.andExpect(status().isUnauthorized());
				}
			}
		}

		@Nested
		@DisplayName("???????????? ?????? ?????? ??????")
		@TestInstance(TestInstance.Lifecycle.PER_CLASS)
		class AfterHistoryDeleted {

			private History deleteHistory;

			@BeforeAll
			void setup () throws Exception {
				deleteHistory = histories.get(0);
				historyService.deleteOrRecoverHistory(testMember, deleteHistory.getId(), true);
			}

			@AfterAll
			void cleanup () throws Exception {
				historyService.deleteOrRecoverHistory(testMember, deleteHistory.getId(), false);
			}

			@Test
			@DisplayName("????????? ???????????? ?????? - ????????? ???")
			@WithUserDetails(
					value = testMemberEmail,
					userDetailsServiceBeanName = "memberService"
			)
			void selectDeletedHistory() throws Exception {
				mockMvc.perform(
						get(apiVersionUrl+"/history/delete")
								.accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", is(deleteHistory.getId().toString())))
				.andExpect(jsonPath("$[0].amount", is(deleteHistory.getAmount().intValue())))
				.andExpect(jsonPath("$[0].summaryMemo", is(deleteHistory.getSummaryMemo())));
			}

			@Test
			@DisplayName("????????? ???????????? ?????? - ???????????? ???")
			void selectDeletedHistoryWithFailAuth() throws Exception {
				mockMvc.perform(
						get(apiVersionUrl+"/history/delete")
								.accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
				)
				.andDo(print())
				.andExpect(status().isUnauthorized());
			}

		}

	}

}