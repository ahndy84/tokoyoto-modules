package com.blogcode.domain.academy;

import com.blogcode.domain.academy.Academy;
import com.blogcode.domain.academy.AcademyRepository;
import com.blogcode.domain.academy.AcademyRepositorySupport;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AcademyRepositoryTest {
	@Autowired
	private AcademyRepository academyRepository;
	@Autowired
	private AcademyRepositorySupport academyRepositorySupport;

	@After
	public void tearDown() throws Exception {
		academyRepository.deleteAllInBatch();
	}

	@Test
	public void querydsl_기본_기능_확인() {
		//given
		String name = "ahndy84";
		String address = "ahndy84@gmail.com";
		academyRepository.save(new Academy(name, address));

		//when
		List<Academy> result = academyRepositorySupport.findByName(name);

		//then
		assertThat(result.size(), is(1));
		assertThat(result.get(0).getAddress(), is(address));
	}

}
