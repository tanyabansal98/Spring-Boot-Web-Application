import { createContext, useContext, useState, useCallback } from 'react';
import axios from 'axios';

// ── Auth-service base URL (separate microservice) ──
const AUTH_API = 'http://localhost:8083/auth';

const AuthContext = createContext(null);

/**
 * AuthProvider — wraps the entire app and provides auth state + actions.
 *
 * Stores token/username/role in React state AND localStorage so that
 * the session survives page refreshes.
 */
export function AuthProvider({ children }) {

    // ── Initialize state from localStorage (survives refresh) ──
    const [token, setToken] = useState(() => localStorage.getItem('token'));
    const [username, setUsername] = useState(() => localStorage.getItem('username'));
    const [role, setRole] = useState(() => localStorage.getItem('role'));

    const isAuthenticated = !!token;

    // ── Login: call auth-service → store JWT ──
    const login = useCallback(async (user, pass) => {
        const res = await axios.post(`${AUTH_API}/login`, {
            username: user,
            password: pass,
        });
        // auth-service returns: { token, username, role }
        const { token: t, username: u, role: r } = res.data;
        localStorage.setItem('token', t);
        localStorage.setItem('username', u);
        localStorage.setItem('role', r);
        setToken(t);
        setUsername(u);
        setRole(r);
    }, []);

    // ── Register: call auth-service → auto-login ──
    const register = useCallback(async (user, pass) => {
        // First register
        await axios.post(`${AUTH_API}/register`, {
            username: user,
            password: pass,
        });
        // Then auto-login to get the JWT token
        await login(user, pass);
    }, [login]);

    // ── Logout: clear everything ──
    const logout = useCallback(() => {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        localStorage.removeItem('role');
        setToken(null);
        setUsername(null);
        setRole(null);
    }, []);

    return (
        <AuthContext.Provider value={{ token, username, role, isAuthenticated, login, register, logout }}>
            {children}
        </AuthContext.Provider>
    );
}

/**
 * useAuth() — custom hook to access auth state from any component.
 * Usage: const { username, login, logout } = useAuth();
 */
export function useAuth() {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error('useAuth() must be used within <AuthProvider>');
    return ctx;
}
