import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api';
import './Enrollments.css';

export default function EnrollmentView() {
    const { studentId } = useParams();
    const navigate = useNavigate();

    const [student, setStudent] = useState(null);
    const [enrollments, setEnrollments] = useState([]);
    const [allCourses, setAllCourses] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    // ── Load student info ──
    useEffect(() => {
        api.get(`/students/${studentId}`)
            .then(res => setStudent(res.data))
            .catch(() => setError('Student not found.'));
    }, [studentId]);

    // ── Load enrollments + all courses ──
    const fetchEnrollments = () => {
        api.get(`/enrollments?studentId=${studentId}`)
            .then(res => setEnrollments(res.data))
            .catch(err => setError(err.response?.data?.error || 'Failed to load enrollments.'));
    };

    useEffect(() => {
        fetchEnrollments();
        api.get('/courses')
            .then(res => setAllCourses(res.data))
            .catch(() => { });
    }, [studentId]);

    // ── Enroll ──
    const handleEnroll = e => {
        e.preventDefault();
        if (!selectedCourse) return;
        setError(''); setSuccess('');
        api.post('/enrollments', { studentId: Number(studentId), courseId: Number(selectedCourse) })
            .then(() => { setSuccess('Enrolled successfully!'); setSelectedCourse(''); fetchEnrollments(); })
            .catch(err => setError(err.response?.data?.error || 'Enrollment failed.'));
    };

    // ── Drop ──
    const handleDrop = (courseId, courseName) => {
        if (!window.confirm(`Drop ${courseName}?`)) return;
        setError(''); setSuccess('');
        api.delete(`/enrollments?studentId=${studentId}&courseId=${courseId}`)
            .then(() => { setSuccess('Dropped successfully!'); fetchEnrollments(); })
            .catch(err => setError(err.response?.data?.error || 'Drop failed.'));
    };

    return (
        <div className="page">
            {/* ── Student Card ── */}
            {student && (
                <div className="student-card">
                    <h2>📋 Enrollments — {student.fullName}</h2>
                    <p>
                        <span>Major: <strong>{student.major || '—'}</strong></span>
                        <span>Status: <span className={`badge ${student.status?.toLowerCase()}`}>{student.status}</span></span>
                        <span>Email: {student.email}</span>
                    </p>
                </div>
            )}

            {error && <div className="msg error">⚠️ {error}</div>}
            {success && <div className="msg success">✅ {success}</div>}

            {/* ── Enroll Form ── */}
            <div className="card">
                <h3>Enroll in a Course</h3>
                <form className="inline-form" onSubmit={handleEnroll}>
                    <select value={selectedCourse} onChange={e => setSelectedCourse(e.target.value)} required>
                        <option value="">-- Choose a course --</option>
                        {allCourses.map(c => (
                            <option key={c.courseId} value={c.courseId}>
                                {c.courseCode} – {c.courseName} ({c.credits} cr)
                            </option>
                        ))}
                    </select>
                    <button type="submit" className="btn-purple">Enroll</button>
                </form>
            </div>

            {/* ── Enrollments Table ── */}
            <h3>Enrolled Courses</h3>
            {enrollments.length === 0
                ? <p className="empty">No courses enrolled yet.</p>
                : (
                    <table>
                        <thead>
                            <tr><th>Code</th><th>Course Name</th><th>Grade</th><th>Enrolled On</th><th>Action</th></tr>
                        </thead>
                        <tbody>
                            {enrollments.map(e => (
                                <tr key={e.enrollmentId}>
                                    <td><strong>{e.courseCode}</strong></td>
                                    <td>{e.courseName}</td>
                                    <td>{e.grade || '—'}</td>
                                    <td>{e.enrolledAt ? new Date(e.enrolledAt).toLocaleDateString() : '—'}</td>
                                    <td>
                                        <button className="btn-delete" onClick={() => handleDrop(e.courseId, e.courseName)}>🗑 Drop</button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )
            }

            <br />
            <button className="btn-secondary" onClick={() => navigate('/students')}>← Back to Students</button>
        </div>
    );
}
