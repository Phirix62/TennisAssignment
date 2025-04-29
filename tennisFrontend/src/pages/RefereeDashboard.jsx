import { useEffect, useState } from 'react';
import api from '../api';
import EditAccountForm from "../components/EditAccountForm";

export default function RefereeDashboard({ username }) {
  const [matches, setMatches] = useState([]);
  const [editing, setEditing] = useState(false);

  useEffect(() => {
    const fetchMatches = async () => {
      const res = await api.get(`/referee/${username}/matches`);
      setMatches(res.data);
    };
    fetchMatches();
  }, [username]);

  const groupedMatches = matches.reduce((acc, match) => {
    const tournament = match.tournament?.name || "Unknown Tournament";
    if (!acc[tournament]) {
      acc[tournament] = [];
    }
    acc[tournament].push(match);
    return acc;
  }, {});

  return (
    <div>
      <h2>Referee Dashboard</h2>
      <button onClick={() => setEditing(!editing)}>
        {editing ? "Close Edit" : "Edit Account"}
      </button>

      {editing && (
        <EditAccountForm
          currentUsername={username}
          onUpdate={(newUsername) => {
            setEditing(false);
            // Optional: refresh or notify App that username changed
          }}
        />
      )}
      <h3>Matchs to supervise</h3>
      {Object.entries(groupedMatches).map(([tournament, matches]) => (
        <div key={tournament}>
          <h4>{tournament}</h4>
          <ul>
            {matches.map((m) => (
              <li key={m.id}>
                {m.player1?.username} vs {m.player2?.username} at {m.matchTime}
              </li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  )
}
