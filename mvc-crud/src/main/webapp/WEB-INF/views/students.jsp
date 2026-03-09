<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <html>

        <head>
            <title>Students</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 20px;
                }

                table {
                    border-collapse: collapse;
                    width: 100%;
                }

                th,
                td {
                    border: 1px solid #ccc;
                    padding: 8px;
                    text-align: left;
                }

                th {
                    background: #4a6fa5;
                    color: white;
                }

                tr:nth-child(even) {
                    background: #f5f5f5;
                }

                .msg-error {
                    color: #c0392b;
                    font-weight: bold;
                    padding: 8px;
                    background: #fdecea;
                    border-radius: 4px;
                }

                .msg-success {
                    color: #27ae60;
                    font-weight: bold;
                    padding: 8px;
                    background: #eafaf1;
                    border-radius: 4px;
                }

                form.inline {
                    display: inline;
                }

                .form-row {
                    display: flex;
                    gap: 8px;
                    flex-wrap: wrap;
                    align-items: flex-end;
                    margin-bottom: 12px;
                }

                .form-row label {
                    display: block;
                    font-size: 12px;
                    color: #555;
                }

                .form-row input,
                .form-row select {
                    padding: 6px;
                    border: 1px solid #ccc;
                    border-radius: 4px;
                }

                button {
                    padding: 6px 14px;
                    background: #4a6fa5;
                    color: white;
                    border: none;
                    border-radius: 4px;
                    cursor: pointer;
                }

                button:hover {
                    background: #3a5a8a;
                }

                a.btn {
                    padding: 5px 10px;
                    background: #e67e22;
                    color: white;
                    text-decoration: none;
                    border-radius: 4px;
                    font-size: 13px;
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
            <h2>👤 Students</h2>

            <c:if test="${not empty error}">
                <p class="msg-error">⚠️ [${statusCode}] ${error}</p>
            </c:if>
            <c:if test="${not empty success}">
                <p class="msg-success">✅ [${statusCode}] ${success}</p>
            </c:if>

            <%-- ── Add Student Form ── --%>
                <h3>Add New Student</h3>
                <form method="post" action="${pageContext.request.contextPath}/students/create">
                    <div class="form-row">
                        <div><label>Full Name *</label><input name="fullName" required placeholder="John Doe" /></div>
                        <div><label>Email</label><input name="email" type="email" placeholder="john@example.com" />
                        </div>
                        <div><label>Phone</label><input name="phone" placeholder="617-555-0000" /></div>
                        <div><label>Date of Birth</label><input name="dob" type="date" /></div>
                        <div><label>Major</label><input name="major" placeholder="Computer Science" /></div>
                        <div>
                            <label>Status</label>
                            <select name="status">
                                <option value="Active">Active</option>
                                <option value="Inactive">Inactive</option>
                                <option value="Graduated">Graduated</option>
                            </select>
                        </div>
                        <div><label>&nbsp;</label><button type="submit">Add Student</button></div>
                    </div>
                </form>

                <%-- ── Students Table ── --%>
                    <table>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Date of Birth</th>
                            <th>Major</th>
                            <th>Status</th>
                            <th>Created</th>
                            <th>Actions</th>
                        </tr>
                        <c:forEach var="s" items="${students}">
                            <tr>
                                <td>${s.student_Id}</td>
                                <td>${s.full_Name}</td>
                                <td>${s.email}</td>
                                <td>${s.phone}</td>
                                <td>${s.date_Of_Birth}</td>
                                <td>${s.major}</td>
                                <td>${s.status}</td>
                                <td>${s.created_At}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/students/edit?id=${s.student_Id}">✏️
                                        Edit</a> &nbsp;
                                    <a class="btn"
                                        href="${pageContext.request.contextPath}/enrollments?studentId=${s.student_Id}">📚
                                        Courses</a> &nbsp;
                                    <form class="inline" method="post"
                                        action="${pageContext.request.contextPath}/students/delete"
                                        onsubmit="return confirm('Delete ${s.full_Name}?')">
                                        <input type="hidden" name="id" value="${s.student_Id}" />
                                        <button type="submit" style="background:#c0392b;">🗑 Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>

        </body>

        </html>