package com.skilldistillery.sdelp.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResourceTest {

	private static EntityManagerFactory emf;
	private EntityManager em;
	private Resource resource;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("SDELP");
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		emf.close();
	}

	@BeforeEach
	void setUp() throws Exception {
		em = emf.createEntityManager();
		resource = em.find(Resource.class, 1);
	}

	@AfterEach
	void tearDown() throws Exception {
		resource = null;
		em.close();
	}

	@Disabled
	@Test
	@DisplayName("Test user entity mapping")
	void test1() {
		assertNotNull(resource);
		assertEquals("test", resource.getTitle());
		assertEquals("www.google.com", resource.getResourceUrl());
		assertEquals(LocalDateTime.of(2020, 01, 23, 00, 00, 00), resource.getCreatedAt());
		assertEquals(LocalDateTime.of(2020, 01, 23, 00, 00, 00), resource.getUpdatedAt());
	}
	
}
