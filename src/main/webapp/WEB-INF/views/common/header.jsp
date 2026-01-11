<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Batting-Stock | 야구 주식 시뮬레이터</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/style.css">

    <style>
        .navbar-brand { font-weight: bold; color: #0d6efd !important; }
        .nav-link:hover { color: #0d6efd !important; }
        body { background-color: #f8f9fa; }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm mb-4">
    <div class="container">
        <a class="navbar-brand" href="/">⚾ Batting-Stock</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="/players">시장 현황</a></li>
                <li class="nav-item"><a class="nav-link" href="/trade">실시간 거래</a></li>
                <li class="nav-item"><a class="nav-link" href="/portfolio">내 지갑</a></li>
            </ul>
            <div class="d-flex align-items-center">
                <span class="badge bg-primary me-2">LIVE</span>
                <small class="text-muted">KBO 데이터 활용 테스트 중</small>
            </div>
        </div>
    </div>
</nav>

<div class="container">