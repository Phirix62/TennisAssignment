import { useEffect, useState } from 'react';
import api from '../api';

export default function AdminDashboard() {
  const [users, setUsers] = useState([]);
  const [editUser, setEditUser] = useState(null);
  const [registrations, setRegistrations] = useState([]);
  const [message, setMessage] = useState('');
  const [processingId, setProcessingId] = useState(null);

  const fetchUsers = async () => {
    const res = await api.get('/admin/users');
    setUsers(res.data);
  };

  const fetchRegistrations = async () => {
    try {
      const res = await api.get('/admin/registrations');
      setRegistrations(res.data);
    } catch {
      setMessage('Failed to fetch registrations.');
    }
  };

  const handleDelete = async (id) => {
    await api.delete(`/admin/users/${id}`);
    fetchUsers();
  };

  const handleEdit = (user) => {
    setEditUser(user);
  };

  const handleUpdate = async (e) => {
    e.preventDefault();
    await api.put(`/admin/users/${editUser.id}`, {
      username: editUser.username,
      email: editUser.email,
      fullName: editUser.fullName
    });
    setEditUser(null);
    fetchUsers();
  };

  const handleDecision = async (id, status) => {
  setProcessingId(id);
  try {
    await api.put(`/admin/registration/${id}/status?status=${status}`);
    fetchRegistrations();
    setMessage(`Registration ${status} successfully.`);
  } catch {
    setMessage('Error updating registration.');
  } finally {
    setProcessingId(null);
  }
};


  useEffect(() => {
    fetchUsers();
    fetchRegistrations();
  }, []);

  return (
    <div style={{ padding: '1rem' }}>
      <h2>Admin Dashboard</h2>
      {message && <p>{message}</p>}

      {/* User Management */}
      <h3>Users</h3>
      <ul>
        {users.map(user => (
          <li key={user.id}>
            <b>{user.username}</b> ({user.email}) - {user.fullName}
            <button onClick={() => handleEdit(user)}>Edit</button>
            <button onClick={() => handleDelete(user.id)}>Delete</button>
          </li>
        ))}
      </ul>

      {editUser && (
        <form onSubmit={handleUpdate}>
          <h4>Edit User</h4>
          <input value={editUser.username} onChange={e => setEditUser({ ...editUser, username: e.target.value })} /><br />
          <input value={editUser.email} onChange={e => setEditUser({ ...editUser, email: e.target.value })} /><br />
          <input value={editUser.fullName} onChange={e => setEditUser({ ...editUser, fullName: e.target.value })} /><br />
          <button type="submit">Update</button>
          <button type="button" onClick={() => setEditUser(null)}>Cancel</button>
        </form>
      )}

      {/* Registration Requests */}
      <h3>Registration Requests</h3>
      {registrations.length === 0 ? (
        <p>No registrations.</p>
      ) : (
        <table border="1" cellPadding="6">
          <thead>
            <tr>
              <th>Player</th>
              <th>Tournament</th>
              <th>Status</th>
              <th>Requested At</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {registrations.map(r => (
              <tr key={r.id}>
                <td>{r.player?.username}</td>
                <td>{r.tournament?.name}</td>
                <td>{r.status}</td>
                <td>{new Date(r.registeredAt).toLocaleString()}</td>
                <td>
                  {r.status === 'PENDING' ? (
                    <>
                      <button disabled={processingId === r.id} onClick={() => handleDecision(r.id, 'ACCEPTED')}>
                        Accept
                      </button>
                      <button disabled={processingId === r.id} onClick={() => handleDecision(r.id, 'REJECTED')}>
                        Reject
                      </button>
                    </>
                  ) : (
                    <span>{r.status}</span>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
