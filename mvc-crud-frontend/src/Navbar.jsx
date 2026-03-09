import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from './AuthContext';
import './Navbar.css';

export default function Navbar() {
    const { pathname } = useLocation();
    const { username, logout } = useAuth();
    const navigate = useNavigate();
    const active = (path) => pathname.startsWith(path) ? 'active' : '';

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav className="navbar">
            <span className="navbar-brand">🎓 MVC CRUD</span>
            <div className="navbar-links">
                <Link className={active('/students')} to="/students">👤 Students</Link>
                <Link className={active('/courses')} to="/courses">📚 Courses</Link>
            </div>
            <div className="navbar-user">
                <span className="navbar-username">👋 {username}</span>
                <button className="btn-logout" onClick={handleLogout}>Logout</button>
            </div>
        </nav>
    );
}
