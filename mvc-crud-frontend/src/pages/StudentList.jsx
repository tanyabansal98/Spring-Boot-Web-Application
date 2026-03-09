import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';
import './Students.css';

export default function StudentList() {
    const [students, setStudents] = useState([]);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [form, setForm] = useState({
        fullName: '', email: '', phone: '', dob: '', major: '', status: 'Active'
    });
    const navigate = useNavigate();

    // ── Fetch all students ──
    const fetchStudents = () => {
        api.get('/students')
            .then(res => setStudents(res.data))
            .catch(err => setError(err.response?.data?.error || 'Failed to load students.'));
    };

    useEffect(() => { fetchStudents(); }, []);

    // ── Handle form input ──
    const handleChange = e =>
        setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));

    // ── Create student ──
    const handleCreate = e => {
        e.preventDefault();
        setError(''); setSuccess('');
        api.post('/students', form)
            .then(() => {
                setSuccess('Student created successfully!');
                setForm({ fullName: '', email: '', phone: '', dob: '', major: '', status: 'Active' });
                fetchStudents();
            })
            .catch(err => setError(err.response?.data?.error || 'Failed to create student.'));
    };

    // ── Delete student ──
    const handleDelete = (id, name) => {
        if (!window.confirm(`Delete ${name}?`)) return;
        setError(''); setSuccess('');
        api.delete(`/students/${id}`)
            .then(() => { setSuccess('Student deleted.'); fetchStudents(); })
            .catch(err => setError(err.response?.data?.error || 'Failed to delete.'));
    };

    return (
        <div className="page">
            <h2>👤 Students</h2>

            {error && <div className="msg error">⚠️ {error}</div>}
            {success && <div className="msg success">✅ {success}</div>}

            {/* ── Add Form ── */}
            <div className="card">
                <h3>Add New Student</h3>
                <form className="grid-form" onSubmit={handleCreate}>
                    <input name="fullName" placeholder="Full Name *" value={form.fullName} onChange={handleChange} required />
                    <input name="email" placeholder="Email" value={form.email} onChange={handleChange} type="email" />
                    <input name="phone" placeholder="Phone" value={form.phone} onChange={handleChange} />
                    <input name="dob" placeholder="Date of Birth" value={form.dob} onChange={handleChange} type="date" />
                    <input name="major" placeholder="Major" value={form.major} onChange={handleChange} />
                    <select name="status" value={form.status} onChange={handleChange}>
                        <option>Active</option>
                        <option>Inactive</option>
                        <option>Graduated</option>
                    </select>
                    <button type="submit" className="btn-primary">Add Student</button>
                </form>
            </div>

            {/* ── Table ── */}
            <table>
                <thead>
                    <tr>
                        <th>ID</th><th>Name</th><th>Email</th><th>Phone</th>
                        <th>DOB</th><th>Major</th><th>Status</th><th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {students.map(s => (
                        <tr key={s.studentId}>
                            <td>{s.studentId}</td>
                            <td>{s.fullName}</td>
                            <td>{s.email}</td>
                            <td>{s.phone}</td>
                            <td>{s.dob}</td>
                            <td>{s.major}</td>
                            <td><span className={`badge ${s.status?.toLowerCase()}`}>{s.status}</span></td>
                            <td className="actions">
                                <button className="btn-edit" onClick={() => navigate(`/students/edit/${s.studentId}`)}>✏️ Edit</button>
                                <button className="btn-enroll" onClick={() => navigate(`/enrollments/${s.studentId}`)}>📚 Courses</button>
                                <button className="btn-delete" onClick={() => handleDelete(s.studentId, s.fullName)}>🗑 Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
