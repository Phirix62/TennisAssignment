import { useEffect, useState } from 'react';
import api from '../api';

export default function RefereeDashboard({ username }) {
  const [matches, setMatches] = useState([]);

  useEffect(() => {
    const fetchMatches = async () => {
      const res = await api.get(`/referee/${username}/matches`);
      setMatches(res.data);
    };
    fetchMatches();
  }, [username]);

  return (
    <div>
      <h2>Referee Dashboard</h2>
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
