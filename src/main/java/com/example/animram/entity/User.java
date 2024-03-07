package com.example.animram.entity;

/*インターフェイスを継承してクラスを定義することを「インターフェイスを実装する」
 * クラスを定義する際にimplememntsキーワードを付けて実装元を指定。UserDetails,UserInf インターフェイスを実装。
 * 実装では、インターフェイスで定義されたメソッドをすべて実装していないと、コンパイルエラーとなります。
 * UserDetailsは、UserDetailsServiceでDBの情報と比較チェックした情報が入ってる。
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.animram.form.UserForm;

import lombok.Data;
import lombok.EqualsAndHashCode;



@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends AbstractEntity implements UserDetails,UserInf {

	public enum Authority {
		ROLE_USER, ROLE_ADMIN
	};

	public User() {
		super();
	}
//引き数１つだけ　UserFormから１つにまとまった情報うけとるから UserForm formだけでいい。
	//空かpathが入る。
	public User(UserForm form,Authority authority,String path, String password) {
		this.email = form.getEmail();
		this.name = form.getName();
		this.avatar = path;
		this.pet_name = form.getPet_name();
		this.pr = form.getPr();
		this.age_id = (long) form.getAgeId();
		this.password = password;
		this.authority = authority;
	}

	

	@Id
	@SequenceGenerator(name = "users_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	
	@Column(nullable = false)
	private String name;

	@Column
	private String avatar;
	
	@Column(nullable = false)
    private Long age_id;

	@Column
	private String pet_name;

	@Column
	private String pr;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Authority authority;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(authority.toString()));
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@OneToOne
    @JoinColumn(name = "age_id", insertable = false, updatable = false)
    private Age age;

	@Override
	public String getUsername() {
		// TODO 自動生成されたメソッド・スタブ
		return this.email;
	}
	
	@Override
    public Long  getUserId(){
    	return this.id;
    }

}

