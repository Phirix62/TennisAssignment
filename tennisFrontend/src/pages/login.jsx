import { useState } from 'react';
import api from '../api';

export default function Login({ onLogin }) {
  const [form, setForm] = useState({ username: '', password: '' });
  const [message, setMessage] = useState('');

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      const res = await api.post('/auth/login', form);
      const role = res.data.includes('Admin') ? 'admin'
                  : res.data.includes('Referee') ? 'referee'
                  : res.data.includes('Player') ? 'player'
                  : null;

      if (role) {
        onLogin({ username: form.username, role });
        setMessage('Login successful');
      } else {
        setMessage('Login failed');
      }
    } catch {
      setMessage('Login failed');
    }
  };

  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <input name="username" placeholder="Username" onChange={handleChange} required /><br />
        <input type="password" name="password" placeholder="Password" onChange={handleChange} required /><br />
        <button type="submit">Login</button>
      </form>
      <p>{message}</p>
    </div>
  );
}
