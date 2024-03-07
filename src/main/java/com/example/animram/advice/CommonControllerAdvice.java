package com.example.animram.advice;

import java.security.Principal;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * CommonControllerAdvice - 各コントローラの共通処理を実装するクラス
 */
@ControllerAdvice
public class CommonControllerAdvice {
  /**
   * addAttributesメソッド - モデル属性にログイン済みか否かのフラグを設定する
   * @param principal - 認証情報
   * @param model - viewに渡されるモデルクラスのインスタンス
   */
  @ModelAttribute
  public void addAttributes(Principal principal, Model model) {
    // principalがnullなら未ログイン
    Boolean loggedIn = null != principal;
    model.addAttribute("loggedIn", loggedIn);
  }
}