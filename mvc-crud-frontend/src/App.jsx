import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './AuthContext';
import Navbar from './Navbar';
import LoginPage from './pages/LoginPage';
import StudentList from './pages/StudentList';
import StudentForm from './pages/StudentForm';
import CourseList from './pages/CourseList';
import EnrollmentView from './pages/EnrollmentView';

/**
 * ProtectedRoute — redirects to /login if not authenticated.
 * Wraps any route that requires a valid JWT token.
 */
function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? children : <Navigate to="/login" replace />;
}

/**
 * AppRoutes — separated so useAuth() is called inside <AuthProvider>.
 */
function AppRoutes() {
  const { isAuthenticated } = useAuth();

  return (
    <>
      {/* Only show Navbar when logged in */}
      {isAuthenticated && <Navbar />}

      <Routes>
        {/* Public route */}
        <Route path="/login" element={
          isAuthenticated ? <Navigate to="/students" replace /> : <LoginPage />
        } />

        {/* Protected routes */}
        <Route path="/" element={<Navigate to="/students" replace />} />
        <Route path="/students" element={
          <ProtectedRoute><StudentList /></ProtectedRoute>
        } />
        <Route path="/students/edit/:id" element={
          <ProtectedRoute><StudentForm /></ProtectedRoute>
        } />
        <Route path="/courses" element={
          <ProtectedRoute><CourseList /></ProtectedRoute>
        } />
        <Route path="/enrollments/:studentId" element={
          <ProtectedRoute><EnrollmentView /></ProtectedRoute>
        } />

        {/* Catch-all: redirect to login */}
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </BrowserRouter>
  );
}
