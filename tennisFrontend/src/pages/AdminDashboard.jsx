import { useEffect, useState } from 'react';
import api from '../api';

export default function AdminDashboard() {
  const [users, setUsers] = useState([]);
  const [editUser, setEditUser] = useState(null);

  const fetchUsers = async () => {
    const res = await api.get('/admin/users');
    setUsers(res.data);
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

  useEffect(() => {
    fetchUsers();
  }, []);

  return (
    <div>
      <h2>Admin Dashboard</h2>
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
          <h3>Edit User</h3>
          <input value={editUser.username} onChange={e => setEditUser({ ...editUser, username: e.target.value })} /><br />
          <input value={editUser.email} onChange={e => setEditUser({ ...editUser, email: e.target.value })} /><br />
          <input value={editUser.fullName} onChange={e => setEditUser({ ...editUser, fullName: e.target.value })} /><br />
          <button type="submit">Update</button>
          <button type="button" onClick={() => setEditUser(null)}>Cancel</button>
        </form>
      )}
    </div>
  );
}
