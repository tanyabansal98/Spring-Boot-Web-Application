import { useState, useEffect } from 'react';
import api from '../api';
import './Courses.css';

export default function CourseList() {
    const [courses, setCourses] = useState([]);
    const [editing, setEditing] = useState(null);   // holds course being edited
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [form, setForm] = useState({
        courseCode: '', courseName: '', credits: 3, instructor: '', department: ''
    });

    const fetchCourses = () => {
        api.get('/courses')
            .then(res => setCourses(res.data))
            .catch(err => setError(err.response?.data?.error || 'Failed to load courses.'));
    };

    useEffect(() => { fetchCourses(); }, []);

    const handleChange = e => {
        const { name, value } = e.target;
        setForm(prev => ({ ...prev, [name]: name === 'credits' ? Number(value) : value }));
    };

    // ── Create ──
    const handleCreate = e => {
        e.preventDefault();
        setError(''); setSuccess('');
        api.post('/courses', form)
            .then(() => {
                setSuccess('Course created!');
                setForm({ courseCode: '', courseName: '', credits: 3, instructor: '', department: '' });
                fetchCourses();
            })
            .catch(err => setError(err.response?.data?.error || 'Failed to create.'));
    };

    // ── Update ──
    const handleUpdate = e => {
        e.preventDefault();
        setError(''); setSuccess('');
        api.put(`/courses/${editing.courseId}`, form)
            .then(() => {
                setSuccess('Course updated!');
                setEditing(null);
                setForm({ courseCode: '', courseName: '', credits: 3, instructor: '', department: '' });
                fetchCourses();
            })
            .catch(err => setError(err.response?.data?.error || 'Failed to update.'));
    };

    // ── Delete ──
    const handleDelete = (id, name) => {
        if (!window.confirm(`Delete ${name}?`)) return;
        setError(''); setSuccess('');
        api.delete(`/courses/${id}`)
            .then(() => { setSuccess('Course deleted.'); fetchCourses(); })
            .catch(err => setError(err.response?.data?.error || 'Failed to delete.'));
    };

    const startEdit = (c) => {
        setEditing(c);
        setForm({
            courseCode: c.courseCode, courseName: c.courseName,
            credits: c.credits, instructor: c.instructor || '', department: c.department || ''
        });
    };

    const cancelEdit = () => {
        setEditing(null);
        setForm({ courseCode: '', courseName: '', credits: 3, instructor: '', department: '' });
    };

    return (
        <div className="page">
            <h2>📚 Courses</h2>

            {error && <div className="msg error">⚠️ {error}</div>}
            {success && <div className="msg success">✅ {success}</div>}

            {/* ── Add / Edit Form ── */}
            <div className="card">
                <h3>{editing ? `Edit: ${editing.courseName}` : 'Add New Course'}</h3>
                <form className="grid-form" onSubmit={editing ? handleUpdate : handleCreate}>
                    <input name="courseCode" placeholder="Code *" value={form.courseCode} onChange={handleChange} required style={{ width: 90 }} />
                    <input name="courseName" placeholder="Course Name *" value={form.courseName} onChange={handleChange} required />
                    <input name="credits" placeholder="Credits" value={form.credits} onChange={handleChange} type="number" min={1} max={6} style={{ width: 70 }} />
                    <input name="instructor" placeholder="Instructor" value={form.instructor} onChange={handleChange} />
                    <input name="department" placeholder="Department" value={form.department} onChange={handleChange} />
                    <div style={{ display: 'flex', gap: 8 }}>
                        <button type="submit" className="btn-green">{editing ? 'Update' : 'Add Course'}</button>
                        {editing && <button type="button" className="btn-secondary" onClick={cancelEdit}>Cancel</button>}
                    </div>
                </form>
            </div>

            {/* ── Table ── */}
            <table>
                <thead>
                    <tr><th>ID</th><th>Code</th><th>Name</th><th>Credits</th><th>Instructor</th><th>Department</th><th>Actions</th></tr>
                </thead>
                <tbody>
                    {courses.map(c => (
                        <tr key={c.courseId}>
                            <td>{c.courseId}</td>
                            <td><strong>{c.courseCode}</strong></td>
                            <td>{c.courseName}</td>
                            <td>{c.credits}</td>
                            <td>{c.instructor}</td>
                            <td>{c.department}</td>
                            <td className="actions">
                                <button className="btn-edit" onClick={() => startEdit(c)}>✏️ Edit</button>
                                <button className="btn-delete" onClick={() => handleDelete(c.courseId, c.courseName)}>🗑 Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
