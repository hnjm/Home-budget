<?php
include "Database.php";

$mysql = new Mysql("mysql.hostinger.pl", "u906935981_pz", "Aq12wS", "u906935981_pz");

if(isset($_POST['delete_user'])){
    $mysql->connect();
    $result = $mysql->deleteUser($_POST['delete_user']);
    if($result === true){
        $response['response_value'] = 1;
        $response['response_message'] = "USER_DELETED";
    }
    else{
        $response['response_value'] = 0;
        $response['response_message'] = $result;
    }
    $mysql->close();
    echo json_encode($response);
}

if(isset($_POST['delete_category'])){
    $mysql->connect();
    if($result = $mysql->deleteCategory($_POST['delete_category'])){
        $response['response_value'] = 1;
        $response['response_message'] = "CATEGORY_DELETED";
    }
    else{
        $response['response_value'] = 0;
        $response['response_message'] = $result;
    }
    $mysql->close();
    echo json_encode($response);
}

if(isset($_POST['delete_element'])){
    $mysql->connect();
    if($result = $mysql->deleteElement($_POST['delete_element'])){
        $response['response_value'] = 1;
        $response['response_message'] = "ELEMENT_DELETED";
    }
    else{
        $response['response_value'] = 0;
        $response['response_message'] = $result;
    }
    $mysql->close();
    echo json_encode($response);
}

if(isset($_POST['delete_settings'])){
    $mysql->connect();
    if($result = $mysql->deleteSettings($_POST['delete_settings'])){
        $response['response_value'] = 1;
        $response['response_message'] = "SETTIGNS_DELETED";
    }
    else{
        $response['response_value'] = 0;
        $response['response_message'] = $result;
    }
    $mysql->close();
    echo json_encode($response);
}
