import { useState } from 'react';
import api from '../api';

export default function Register() {
  const [form, setForm] = useState({
    username: '',
    password: '',
    email: '',
    fullName: '',
    role: 'player'
  });
  const [message, setMessage] = useState('');

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      const res = await api.post('/auth/register', form);
      setMessage(res.data);
    } catch (err) {
      setMessage(err.response?.data || 'Registration failed.');
    }
  };

  return (
    <div>
      <h2>Register</h2>
      <form onSubmit={handleSubmit}>
        <input name="username" placeholder="Username" onChange={handleChange} required /><br />
        <input type="password" name="password" placeholder="Password" onChange={handleChange} required /><br />
        <input name="email" placeholder="Email" onChange={handleChange} required /><br />
        <input name="fullName" placeholder="Full Name" onChange={handleChange} required /><br />
        <select name="role" onChange={handleChange}>
          <option value="player">Player</option>
          <option value="referee">Referee</option>
          <option value="admin">Admin</option>
        </select><br />
        <button type="submit">Register</button>
      </form>
      <p>{message}</p>
    </div>
  );
}
