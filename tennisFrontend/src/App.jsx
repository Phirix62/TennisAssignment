import Register from './pages/Register';
import Login from './pages/Login';
import AdminDashboard from './pages/AdminDashboard';
import PlayerDashboard from './pages/PlayerDashboard';
import RefereeDashboard from './pages/RefereeDashboard';
import { useState } from 'react';

export default function App() {
  const [page, setPage] = useState('register');
  const [loggedInUser, setLoggedInUser] = useState(null);

  const handleLogout = () => {
    setLoggedInUser(null);
    setPage('login');
  };

  return (
    <div style={{ padding: 20 }}>
      <h1>Tennis Tournament App</h1>

      <div style={{ marginBottom: 20 }}>
        {!loggedInUser && (
          <>
            <button onClick={() => setPage('register')}>Register</button>
            <button onClick={() => setPage('login')}>Login</button>
          </>
        )}
        {loggedInUser && (
          <>
            <span>Welcome, {loggedInUser.username} ({loggedInUser.role})</span>
            <button onClick={handleLogout}>Logout</button>
          </>
        )}
      </div>

      {!loggedInUser && page === 'register' && <Register />}
      {!loggedInUser && page === 'login' && <Login onLogin={setLoggedInUser} />}

      {loggedInUser?.role === 'admin' && <AdminDashboard />}
      {loggedInUser?.role === 'player' && <PlayerDashboard username={loggedInUser.username} />}
      {loggedInUser?.role === 'referee' && <RefereeDashboard username={loggedInUser.username} />}
    </div>
  );
}
