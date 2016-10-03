<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div>
    <h2>A global</h2>
    <h3>Look at</h3>
    <ol class="spittle-list">
        <c:forEach var="spittle" items="${spittles}">
            <s:url value="/spitters/{spittlerName}" var="spitter_url" >
                <s:param name="spittlerName" value="${spittle.spitter.username}" />
            </s:url>

        </c:forEach>
    </ol>
</div>
