package com.example.animram.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.animram.entity.Age;
import com.example.animram.entity.User;
import com.example.animram.entity.User.Authority;
import com.example.animram.form.UserForm;
import com.example.animram.repository.AgeRepository;
import com.example.animram.repository.UserRepository;

@Controller
public class UsersController {

  //入力されたパスワードを暗号化する為注入。
  @Autowired
  private PasswordEncoder passwordEncoder;

  //新規ユーザー情報入力の際、e-mailがダブってないか見るために記入。
  @Autowired
  private UserRepository repository;

  //新規ユーザー入力画面遷移の際、DBから年代を取ってくる。
  @Autowired
  private AgeRepository ageRepository;

  //@Valueアノテーションを付与することで、外部のプロパティ、つまり外部パラメータymlを直接設定できる。
  @Value("${image.local:false}")
  private String imageLocal;

  //絶対パスを取得するため注入。
  @Autowired
  private HttpServletRequest request;

  //ユーザー情報新規投稿画面遷移メソッド。
  @GetMapping("/users/new")
  public String newUser(Model model) {
    model.addAttribute("form", new UserForm());
    List<Age> ages = ageRepository.findAll();
    model.addAttribute("ages", ages);
    return "users/new";
  }

  //新規ユーザー入力情報をデータベースに保存する。
  @PostMapping("/users")
  public String create(@Validated @ModelAttribute("form") UserForm form, BindingResult result,
      Model model, MultipartFile ava,
      RedirectAttributes redirAttrs) throws IOException {

    //入力情報をフィールドに設定する。加工が必要ない情報はかかなくてもユーザー登録できた。
    String name = form.getName();
    String email = form.getEmail();
    //String avatar = form.getAvatar(); 画像はそのままでは使えないのでここでは書かなくていい。
    String pet_name = form.getPet_name();
    //入力されたパスワードはpasswordEncoderのencodeメソッドで暗号化する。
    String password = passwordEncoder.encode(form.getPassword());
    String passwordConfirmation = form.getPasswordConfirmation();
    String pr = form.getPr();
    Long age_id = form.getAgeId();

    //BindingResultをController側で操作する際のコードを手動で書く。本来はヴァリデーションエラーが良い？
    //自作のヴァリデーションエラーを作成後、propertiesにエラーメッセージを追記。
    // フィールドのエラーを表し、Formクラス名，フィールド名，エラーメッセージを渡す。
    // getObjectNameではフォームクラス名が取得できる。
    if (repository.findByEmail(email) != null) {

      FieldError fieldError = new FieldError(result.getObjectName(), "email", "その E メールはすでに使用されています。");
      // エラーを追加する。
      result.addError(fieldError);

    }
    //e-mailのダブリを含む入力情報にエラーがあればメッセ―ジ表示し、新規情報入力画面へ遷移。
    if (result.hasErrors()) {
      model.addAttribute("hasMessage", true);
      model.addAttribute("class", "alert-danger");
      model.addAttribute("message", "ユーザー登録に失敗しました。");
      List<Age> ages = ageRepository.findAll();
      model.addAttribute("ages", ages);
      return "users/new";
    }

    //画像保存処理。警告出てるのは古いから。
    boolean isImageLocal = false;
    if (imageLocal != null) {
      isImageLocal = new Boolean(imageLocal);
    }
    //if文の外でdestFile、path初期化
    File destFile = null;
    //空文字。""が、入ってる。空はnullではない。
    String path = "";

    //ymlでの設定がtrueなら続行。
    if (isImageLocal) {
      //saveImageLocalメソッドでローカル保処理した画像をdestFileに代入する。
      //もし画像が選択されていれば119～124行目の処理実行。!ava.isEmptyでユーザーが画像選択してないと判断。
      if (!ava.isEmpty()) {
        destFile = saveImageLocal(ava);
        //保存した画像が入ってるファイルの場所を表す絶対パスをgetAbsolutePathで取得。絶対パスでないとDBに保存出来ない？
        //ファイルそのものではなく場所をDBに保存する。※画像はdataが重いから。
        //pathが空文字から、別の値にすり替えてる。再代入
        path = destFile.getAbsolutePath();

      }

    }
    // User entityをインスタンスして、ユーザー新規登録に必要な情報をentityに詰める。
    //画像選択しない場合、119～124行目の処理実行されないので、113行目で設定した空文字が引き数pathに入る。
    User entity = new User(form, Authority.ROLE_USER, path, password);
    repository.saveAndFlush(entity);
    model.addAttribute("hasMessage", true);
    model.addAttribute("class", "alert-info");
    model.addAttribute("message", "ユーザー登録が完了しました。");
    return "pages/index";
  }

  //画像を指定のローカルに保存するためのメソッド。
  //saveImageLocalは画像選択してほしい場合だけ動いてほしい。""のままなら動かない。画像保存前提で動いてほしい。
  private File saveImageLocal(MultipartFile ava)
      throws IOException {
    //129 130行目書かなくても画像保存できた。uploadDir.mkdir();はディレクトリー作成。
    File uploadDir = new File("/uploads");
    uploadDir.mkdir();

    //ディレクトリーの名前を決める。
    String uploadsDir = "/uploads/";

    //request.getServletContext().getRealPath(uploadsDir);と書くことで指定した仮想パスに対応する、ファイルシステム上の絶対パスを返します。例:http://localhost:8080/Context/directory/test.txtのようにフルパス。
    //ディレクトリーまでの絶対パス名を取得。
    String realPathToUploads = request.getServletContext().getRealPath(uploadsDir);
    //existメソッドでファイル(フルパス)の存在を確かめ、存在がなければ新しいファイルを作成する
    if (!new File(realPathToUploads).exists()) {
      //fileがなかったら作成する。
      new File(realPathToUploads).mkdir();
    }
    //投稿アバターの名前を取得。
    String fileName = ava.getOriginalFilename();
    //親抽象パス名と子パス名文字列。もし親がnullならば、子パスでFileインスタンスが生成される。C:/uploads(親パス)/○○(子パス)
    File destFile = new File(realPathToUploads, fileName);
    //C:/uploads(親パス)/○○(子パス)で画像を保存する。
    ava.transferTo(destFile);

    return destFile;
  }
}
