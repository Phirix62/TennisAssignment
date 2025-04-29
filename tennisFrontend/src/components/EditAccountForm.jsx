import { useState } from 'react';
import api from '../api';

export default function EditAccountForm({ currentUsername, onUpdate }) {
  const [form, setForm] = useState({
    username: '',
    email: '',
    fullName: ''
  });
  const [message, setMessage] = useState('');

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      const res = await api.put(`/account/${currentUsername}/update`, form);
      setMessage(res.data);
      onUpdate(form.username); // notify parent if username changed
    } catch {
      setMessage('Update failed.');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h3>Edit Account Info</h3>
      <input name="username" placeholder="New Username" onChange={handleChange} required /><br />
      <input name="email" placeholder="New Email" onChange={handleChange} required /><br />
      <input name="fullName" placeholder="New Full Name" onChange={handleChange} required /><br />
      <button type="submit">Save</button>
      <p>{message}</p>
    </form>
  );
}
