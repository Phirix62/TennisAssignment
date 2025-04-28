import { useEffect, useState } from 'react';
import api from '../api';

export default function PlayerDashboard({ username }) {
  const [matches, setMatches] = useState([]);
  const [tournaments, setTournaments] = useState([]);
  const [selectedTournament, setSelectedTournament] = useState('');
  const [message, setMessage] = useState('');

  // Fetch matches for player
  const fetchMatches = async () => {
    const res = await api.get(`/player/${username}/matches`);
    setMatches(res.data);
  };

  // Fetch available tournaments
  const fetchTournaments = async () => {
    const res = await api.get('/tournaments'); // (We will create a simple GET all tournaments)
    setTournaments(res.data);
  };

  useEffect(() => {
    fetchMatches();
    fetchTournaments();
  }, []);

  // Join tournament
  const handleJoin = async () => {
    try {
      await api.post(`/player/${username}/join/${selectedTournament}`);
      setMessage('Successfully joined tournament!');
      fetchMatches(); // refresh matches after joining
    } catch {
      setMessage('Failed to join tournament.');
    }
  };

  return (
    <div>
      <h2>Player Dashboard</h2>

      <h3>Join a Tournament</h3>
      <select onChange={(e) => setSelectedTournament(e.target.value)} value={selectedTournament}>
        <option value="">Select Tournament</option>
        {tournaments.map(t => (
          <option key={t.id} value={t.id}>{t.name} - {t.location}</option>
        ))}
      </select>
      <button onClick={handleJoin} disabled={!selectedTournament}>Join</button>
      <p>{message}</p>

      <h3>Your Matches</h3>
      <ul>
        {matches.map(m => (
          <li key={m.id}>
            {m.player1?.username} vs {m.player2?.username} at {m.matchTime}
          </li>
        ))}
      </ul>
    </div>
  );
}
