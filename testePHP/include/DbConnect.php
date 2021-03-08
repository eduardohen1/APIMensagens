<?php
class DbConnect{
   private $conn;
   function __construct(){
   }
   /**
    * Estabelecendo conexão com o banco de dados
    * @return database connection handler
    */
    function connect(){
       require_once('Confif.php');

       $this->conn = pg_connect("host=DB_HOST port=DB_PORT dbname=DB_NAME user=DB_USERNAME password=DB_PASSWORD");
       return $this->conn;
    }

}