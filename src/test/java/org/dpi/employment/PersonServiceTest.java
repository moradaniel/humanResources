package org.dpi.employment;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.dpi.person.Person;
import org.dpi.person.PersonQueryFilter;
import org.dpi.person.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:**/PersonServiceTest-context.xml",
									"classpath*:**/spring/root-context.xml",
									"classpath*:**/GenericDaoContext.xml",
									"classpath*:**/SecurityContextGeneric.xml",
									"file:src/main/webapp/WEB-INF/controllers.xml"
})


@ActiveProfiles(profiles= { "test" })
public class PersonServiceTest {
	final Logger logger = LoggerFactory.getLogger(PersonServiceTest.class);
	
	
	@Autowired
	private PersonService personService = null;
	
	
	@Test
	public void testPersonService() {
		
		PersonQueryFilter personQueryFilter = new PersonQueryFilter();
		personQueryFilter.setCuil("23102239989");
		
		List<Person> persons = personService.find(personQueryFilter);
		
		assertThat(persons, hasSize(1));
		
		Person person = persons.get(0);
		
		assertNotNull("Person is null", person);
		
	}
}