<?php
class DbOperation {
   private $conn;
   function __construct(){
      require_once dirname(__FILE__) . '/Config.php';
      require_once dirname(__FILE__) . '/DbConnection.php';
      $db = new DbConnect();
      $this->conn = $db->connect();
   }

   //Função para criar usuário
   public function createUser($name, $email){
      if(!$this->isUserExists($email)){
         $dados = array('name' => '$name', 'email' => '$email');
         $result = pg_insert($this->conn, 'users', $dados);
         if(!$result){
            return USER_CREATE_FAILED;
         }else{
            return USER_CREATED_SUCCESSFULLY;
         }
      }else{
         return USER_ALREADY_EXISTED;
      }
   }

   //Função para recuperar o usuário com o email
   public function getUser($email){
      $response = "";
      $result = pg_query($this->conn,"SELECT name FROM users WHERE email = '$email'");
      if($result){
         $rs = pg_fetch_row($result);
         if($rs) $response = $rs[0];
      }
      return $response;
   }

   //Função para verificar se usuário existe:
   public function isUserExists($email){
      $response = false;
      $result = pg_query($this->conn, "SELECT id FROM users WHERE email = '$email'");
      if($result){
         $rs = pg_fetch_row($result);
         if($rs) $response = true;
      }
      return $response;
   }

   //Funão para atualizar token no BD
   public function storeGCMToken($id, $token){
      $dados = array('gcmtoken'=>'$token');
      $condicao = array('id'=>'$id');
      $result = pg_update($this->conn, 'users',$_POST,$dados, $condicao);
      if($result){
         return true;
      }else{
         return false;
      }
   }

   public function getRegistrationTokens($id){
      $response ="";
      $result = pg_query($this->conn, "SELECT gcmtoken FROM users WHERE id = $id");
      if($result){
         $rs = pg_fetch_row($result);
         if($rs) $response = $rs[0];
      }
      return $response;
   }

   public function addMessage($id, $message){
      $dados = array('message' => '$message', 'users_id' => '$id');
      $result = pg_insert($this->conn, 'messages',$_POST, $dados);
      if($result){
         return true;
      }else{
         return false;
      }
   }

   public function getMessages(){
      $result = pg_query($this->conn, "SELECT m.id, m.message, m.sentat, m.users_id, u.name FROM messages m INNER JOIN users u ON m.users_id = u.id ORDER BY m.id ASC");
      if($result){
         return $result;
      }else{
         return null;
      }
   }

}