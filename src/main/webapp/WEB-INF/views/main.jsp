<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="common/header.jsp" %>

<div class="row">
    <div class="col-12 text-center py-5">
        <h1 class="display-4">⚾ 오늘 최고의 타자는 누구?</h1>
        <p class="lead">실시간 안타 한 방에 내 자산이 움직입니다.</p>
        <a href="/players" class="btn btn-primary btn-lg mt-3">지금 투자하기</a>
        <div id="live-ticker">
            <div id="ticker-content">
                📢 실시간 시황: 시뮬레이션 엔진 가동 중... 잠시 후 주가 변동이 시작됩니다!
            </div>
        </div>
    </div>
</div>

<div class="row mt-4">
    <div class="col-md-6">
        <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">🔥 실시간 핫 플레이어</div>
            <ul id="realtime-logs" class="list-group list-group-flush">
                <li class="list-group-item text-center text-muted">경기 중계를 불러오고 있습니다...</li>
            </ul>
        </div>
    </div>
</div>

<script>
    // SSE 연결 시작
    const eventSource = new EventSource('/api/v1/stock/stream');

    eventSource.addEventListener('connect', (e) => {
        console.log("===== SSE 연결 성공:", e.data);
    });

    eventSource.addEventListener('playerRecordUpdate', (e) => {
        const data = JSON.parse(e.data); // 파싱

        if(data && data.length > 0) {
            const top = data[0];
            const color = top.outcome === 'HOMERUN' ? 'text-danger' : 'text-warning';
            $('#ticker-content').html(`<span class="\${color}">[\${top.outcome}]</span> \${top.playerName} 선수 시세 변동! 현재가: \${top.changedPrice}원`);

            // 업데이트 (최신 5건)
            let html = "";
            data.forEach(log => {
                const fluctuation = log.fluctuation || 0;
                const badgeClass = fluctuation > 0 ? 'bg-danger' : 'bg-primary';
                const sign = fluctuation > 0 ? '↑' : '↓';

                html += `<li class="list-group-item d-flex justify-content-between align-items-center">
                        \${log.playerName} (\${log.outcome})
                        <span class="badge \${badgeClass} rounded-pill">\${sign} \${Math.abs(fluctuation)}원</span>
                     </li>`;
            });
            $('#realtime-logs').html(html);
        }
    });

    // 에러 발생 시 처리
    eventSource.onerror = (error) => {
        console.error("===== SSE 연결 오류 : ", error);
    };

    // 페이지를 닫거나 벗어날 때 연결 종료
    window.addEventListener('beforeunload', () => {
        eventSource.close();
    });
</script>
<%@ include file="common/footer.jsp" %>