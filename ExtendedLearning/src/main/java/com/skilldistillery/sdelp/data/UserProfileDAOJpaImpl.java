package com.skilldistillery.sdelp.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.skilldistillery.sdelp.entities.Profile;
import com.skilldistillery.sdelp.entities.User;

@Transactional
@Service
public class UserProfileDAOJpaImpl implements UserProfileDAO {
	
	@PersistenceContext
	private EntityManager em;
	

	@Override
	public User getUserByUsernamePassword(String username, String password) {
		User user = null;
		String jpql = "select user from User user where username = :username"
				+ " and password = :password";
		user = em.createQuery(jpql, User.class).setParameter("username", username)
				.setParameter("password", password).getResultList().get(0);
		return user;
	}
	
	@Override
	public boolean checkIfUsernameAndEmailAreAvailable(String username, String email) {
		boolean isAvail = true;
		String jpql = "select user from User user where username = :username";
		if (em.createQuery(jpql, User.class).setParameter("username", username).getResultList().size() != 0) {
			isAvail = false;
		}
		jpql = "select profile from Profile profile where email = :email";
		if (em.createQuery(jpql, Profile.class).setParameter("email", email).getResultList().size() != 0) {
			isAvail = false;
		}
		return isAvail;
	}

	@Override
	public User createUser(User user) {
		em.persist(user);
		em.flush();
		return user;
	}

	@Override
	public User updateUser(User user) {
		User managedUser = em.find(User.class, user.getId());
		managedUser.setUsername(user.getUsername());
		managedUser.setPassword(user.getPassword());
		managedUser.setRole(user.getRole());
		managedUser.setActive(user.getActive());
		em.flush();
		return managedUser;
	}
	
	@Override
	public Profile getProfileByUserUsernamePassword(String username, String password) {
		Profile profile = null;
		String jpql = "SELECT profile from Profile profile WHERE profile.user.username = :username"
				+ " AND profile.user.password = :password";
		profile = em.createQuery(jpql, Profile.class).setParameter("username", username)
				.setParameter("password", password).getResultList().get(0);
		return profile;
	}
	
	@Override
	public Profile createProfile(Profile profile) {
		em.persist(profile);
		em.flush();
		return profile;
	}
	
	@Override
	public Profile updateProfile(Profile profile) {
		Profile managedProfile = em.find(Profile.class, profile.getId());
		managedProfile.setFirstName(profile.getFirstName());
		managedProfile.setLastName(profile.getLastName());
		managedProfile.setEmail(profile.getEmail());
		managedProfile.setJobTitle(profile.getJobTitle());
		managedProfile.setAbout(profile.getAbout());
		em.flush();
		return managedProfile;
	}
	
	@Override
	public List<User> getAllNonAdminUsers(){
		String jpql = "select user from User user where user.role = 'USER'";
		List<User> nonAdminUsers = em.createQuery(jpql, User.class).getResultList();
		return nonAdminUsers;
	}
	
	public User getUserById(int uid) {
		User user = em.find(User.class, uid);
		return user;
	}

}
