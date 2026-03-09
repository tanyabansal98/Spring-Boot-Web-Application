import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import './Login.css';

export default function LoginPage() {
    const [isRegister, setIsRegister] = useState(false);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const { login, register } = useAuth();
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            if (isRegister) {
                await register(username, password);
            } else {
                await login(username, password);
            }
            navigate('/students'); // redirect on success
        } catch (err) {
            const msg = err.response?.data?.error
                || err.response?.data?.message
                || (isRegister ? 'Registration failed.' : 'Invalid username or password.');
            setError(msg);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-page">
            <div className="login-card">
                <div className="login-header">
                    <span className="login-icon">🔐</span>
                    <h2>{isRegister ? 'Create Account' : 'Welcome Back'}</h2>
                    <p className="login-subtitle">
                        {isRegister
                            ? 'Register to access the MVC CRUD application'
                            : 'Sign in to your account'}
                    </p>
                </div>

                {error && <div className="login-error">⚠️ {error}</div>}

                <form onSubmit={handleSubmit} className="login-form">
                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <input
                            id="username"
                            type="text"
                            placeholder="Enter your username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                            autoFocus
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            id="password"
                            type="password"
                            placeholder={isRegister ? 'Min. 6 characters' : 'Enter your password'}
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            minLength={isRegister ? 6 : undefined}
                        />
                    </div>

                    <button type="submit" className="btn-login" disabled={loading}>
                        {loading
                            ? '⏳ Please wait...'
                            : isRegister ? '🚀 Register' : '🔑 Sign In'}
                    </button>
                </form>

                <div className="login-toggle">
                    {isRegister ? 'Already have an account?' : "Don't have an account?"}
                    <button
                        type="button"
                        className="btn-toggle"
                        onClick={() => { setIsRegister(!isRegister); setError(''); }}
                    >
                        {isRegister ? 'Sign In' : 'Register'}
                    </button>
                </div>
            </div>
        </div>
    );
}
