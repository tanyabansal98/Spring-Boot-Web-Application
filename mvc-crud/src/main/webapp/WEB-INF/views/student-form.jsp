<%@ page contentType="text/html;charset=UTF-8" %>
  <%@ taglib prefix="c" uri="jakarta.tags.core" %>
    <html>

    <head>
      <title>Edit Student</title>
      <style>
        body {
          font-family: Arial, sans-serif;
          margin: 20px;
        }

        .form-grid {
          display: grid;
          grid-template-columns: 150px 1fr;
          gap: 10px;
          align-items: center;
          max-width: 480px;
        }

        input,
        select {
          padding: 6px;
          border: 1px solid #ccc;
          border-radius: 4px;
          width: 100%;
          box-sizing: border-box;
        }

        button {
          padding: 8px 20px;
          background: #4a6fa5;
          color: white;
          border: none;
          border-radius: 4px;
          cursor: pointer;
          margin-top: 10px;
        }

        button:hover {
          background: #3a5a8a;
        }

        .msg-error {
          color: #c0392b;
          font-weight: bold;
        }

        nav a {
          margin-right: 12px;
          color: #4a6fa5;
          text-decoration: none;
          font-weight: bold;
        }
      </style>
    </head>

    <body>
      <nav>
        <a href="${pageContext.request.contextPath}/">🏠 Home</a>
        <a href="${pageContext.request.contextPath}/students">👤 Students</a>
        <a href="${pageContext.request.contextPath}/courses">📚 Courses</a>
      </nav>
      <hr />
      <h2>✏️ Edit Student</h2>

      <c:if test="${not empty error}">
        <p class="msg-error">⚠️ [${statusCode}] ${error}</p>
      </c:if>

      <form method="post" action="${pageContext.request.contextPath}/students/update">
        <input type="hidden" name="id" value="${student.student_Id}" />
        <div class="form-grid">
          <label>Full Name *</label>
          <input name="fullName" value="${student.full_Name}" required />

          <label>Email</label>
          <input name="email" type="email" value="${student.email}" />

          <label>Phone</label>
          <input name="phone" value="${student.phone}" placeholder="617-555-0000" />

          <label>Date of Birth</label>
          <input name="dob" type="date" value="${student.date_Of_Birth}" />

          <label>Major</label>
          <input name="major" value="${student.major}" placeholder="Computer Science" />

          <label>Status</label>
          <select name="status">
            <option value="Active" ${student.status=='Active' ? 'selected' : '' }>Active</option>
            <option value="Inactive" ${student.status=='Inactive' ? 'selected' : '' }>Inactive</option>
            <option value="Graduated" ${student.status=='Graduated' ? 'selected' : '' }>Graduated</option>
          </select>
        </div>
        <button type="submit">💾 Update Student</button>
      </form>

      <br />
      <a href="${pageContext.request.contextPath}/students">← Back to Students</a>
    </body>

    </html>