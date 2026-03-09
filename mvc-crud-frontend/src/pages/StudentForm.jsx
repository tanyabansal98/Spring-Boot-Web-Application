import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api';
import './Students.css';

export default function StudentForm() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [error, setError] = useState('');
    const [form, setForm] = useState({
        fullName: '', email: '', phone: '', dob: '', major: '', status: 'Active'
    });

    // ── Load existing student ──
    useEffect(() => {
        if (!id) return;
        api.get(`/students/${id}`)
            .then(res => {
                const s = res.data;
                setForm({
                    fullName: s.fullName || '',
                    email: s.email || '',
                    phone: s.phone || '',
                    dob: s.dob ? s.dob.substring(0, 10) : '',
                    major: s.major || '',
                    status: s.status || 'Active',
                });
            })
            .catch(() => setError('Failed to load student.'));
    }, [id]);

    const handleChange = e =>
        setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));

    const handleSubmit = e => {
        e.preventDefault();
        setError('');
        api.put(`/students/${id}`, form)
            .then(() => navigate('/students'))
            .catch(err => setError(err.response?.data?.error || 'Update failed.'));
    };

    return (
        <div className="page">
            <h2>✏️ Edit Student</h2>
            {error && <div className="msg error">⚠️ {error}</div>}

            <div className="card" style={{ maxWidth: 480 }}>
                <form className="stack-form" onSubmit={handleSubmit}>
                    <label>Full Name *</label>
                    <input name="fullName" value={form.fullName} onChange={handleChange} required />

                    <label>Email</label>
                    <input name="email" type="email" value={form.email} onChange={handleChange} />

                    <label>Phone</label>
                    <input name="phone" value={form.phone} onChange={handleChange} />

                    <label>Date of Birth</label>
                    <input name="dob" type="date" value={form.dob} onChange={handleChange} />

                    <label>Major</label>
                    <input name="major" value={form.major} onChange={handleChange} />

                    <label>Status</label>
                    <select name="status" value={form.status} onChange={handleChange}>
                        <option>Active</option>
                        <option>Inactive</option>
                        <option>Graduated</option>
                    </select>

                    <div className="form-actions">
                        <button type="submit" className="btn-primary">💾 Save Changes</button>
                        <button type="button" className="btn-secondary" onClick={() => navigate('/students')}>Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    );
}
