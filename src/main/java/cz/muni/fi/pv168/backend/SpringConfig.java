package cz.muni.fi.pv168.backend;

import cz.muni.fi.pv168.backend.entities.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.DERBY;

@Configuration
@EnableTransactionManagement
public class SpringConfig {

	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
				.setType(DERBY)
				//.addScript("droptables.sql")
				.addScript("tables.sql")
				.addScript("test-data.sql")
				.build();
	}

	@Bean //potřeba pro @EnableTransactionManagement
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean //náš manager, bude obalen řízením transakcí
	public SpyManager spyManager() {
		return new SpyManagerImpl(dataSource());
	}

	@Bean
	public MissionManager missionManager() {
		return new MissionManagerImpl(new TransactionAwareDataSourceProxy(dataSource())); // na co je toto?
	}

	@Bean
	public AgencyManager agencyManager() {
		AgencyManagerImpl agencyManager = new AgencyManagerImpl(dataSource());
		agencyManager.setSpyManager(spyManager());
		agencyManager.setMissionManager(missionManager());
		return agencyManager;
	}
}
