package person;

import builder.ddl.DDLBuilderData;
import builder.ddl.builder.CreateQueryBuilder;
import builder.ddl.builder.DropQueryBuilder;
import builder.ddl.dataType.DB;
import database.H2DBConnection;
import entity.Person;
import jdbc.JdbcTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.EntityManagerImpl;
import service.person.PersonService;
import service.person.request.PersonRequest;
import service.person.response.PersonResponse;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/*
- Person 1L 데이터를 가져온다.
- Person 데이터를 가져올 시 존재하지 않는 데이터면 RuntimeException 이 발생한다.
- Person 1L 데이터를 삭제한다.
*/
class PersonServiceTest {

    private PersonService personService;
    private H2DBConnection h2DBConnection;
    private JdbcTemplate jdbcTemplate;

    //정확한 테스트를 위해 메소드마다 DB실행 후 테이블생성
    @BeforeEach
    void eachSetUp() throws SQLException {
        this.h2DBConnection = new H2DBConnection();

        this.jdbcTemplate = this.h2DBConnection.start();

        //테이블 생성
        CreateQueryBuilder queryBuilder = new CreateQueryBuilder();
        String createQuery = queryBuilder.buildQuery(DDLBuilderData.createDDLBuilderData(Person.class, DB.H2));

        jdbcTemplate.execute(createQuery);

        this.personService = new PersonService(new EntityManagerImpl(jdbcTemplate));

        this.personService.save(createPersonRequest(1));
        this.personService.save(createPersonRequest(2));
    }

    //정확한 테스트를 위해 메소드마다 테이블 DROP 후 DB종료
    @AfterEach
    void tearDown() {
        DropQueryBuilder queryBuilder = new DropQueryBuilder();
        String dropQuery = queryBuilder.buildQuery(DDLBuilderData.createDDLBuilderData(Person.class, DB.H2));
        jdbcTemplate.execute(dropQuery);
        this.h2DBConnection.stop();
    }

    @DisplayName("Person 1L 데이터를 가져온다.")
    @Test
    void findByIdTest() {
        PersonResponse personResponse = personService.findById(1L);

        assertThat(personResponse)
                .extracting("id", "name", "age", "email")
                .containsExactly(1L, "test1", 29, "test@test.com");
    }

    @DisplayName("Person 데이터를 가져올 시 존재하지 않는 데이터면 RuntimeException 이 발생한다.")
    @Test
    void findByIdThrowExceptionTest() {
        assertThatThrownBy(() -> personService.findById(3L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Expected 1 result, got 0");
    }

    @DisplayName("Person 1L 데이터를 삭제한다.")
    @Test
    void deleteByIdTest() {
        personService.deleteById(1L);
        assertThatThrownBy(() -> personService.findById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Expected 1 result, got 0");
    }

    private PersonRequest createPersonRequest(int i) {
        return new PersonRequest((long) i, "test" + i, 29, "test@test.com");
    }
}
