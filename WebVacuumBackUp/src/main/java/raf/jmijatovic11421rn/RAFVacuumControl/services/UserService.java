package raf.jmijatovic11421rn.RAFVacuumControl.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import raf.jmijatovic11421rn.RAFVacuumControl.model.User;
import raf.jmijatovic11421rn.RAFVacuumControl.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        return this.userRepository.save(user);
    }

    public User update(User user) {
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        return this.userRepository.update(user);
    }

    public void deleteUser(String email) {
        this.userRepository.deleteByEmail(email);
    }

    public Page<User> paginate(Integer page, Integer size) {
        return this.userRepository.findAll(PageRequest.of(page, size));
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User findByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Not found!");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

}
