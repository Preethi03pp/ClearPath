import React, { useEffect, useState } from 'react'
import api from '../api'
import { useAuth } from '../context/AuthContext'

export default function Teams() {
  const { user, loading } = useAuth()
  const [list, setList] = useState([])
  const [t, setT] = useState({ name:'', memberIds:'' })
  const [pageLoading, setPageLoading] = useState(true)

  useEffect(() => {
    if (loading || !user) return
    setPageLoading(true)
    api.get('/api/teams')
      .then(r => setList(r.data))
      .catch(console.error)
      .finally(() => setPageLoading(false))
  }, [loading, user])

  const create = async e => {
    e.preventDefault()
    const payload = { 
      name: t.name, 
      memberIds: t.memberIds.split(',').map(x => x.trim()).filter(Boolean) 
    }
    await api.post('/api/teams', payload)
    setT({ name:'', memberIds:'' })
    // reload team list after create
    setPageLoading(true)
    api.get('/api/teams')
      .then(r => setList(r.data))
      .catch(console.error)
      .finally(() => setPageLoading(false))
  }

  if (loading || pageLoading) {
    return <div style={{ textAlign: 'center', padding: '2rem' }}>Loading teams...</div>
  }

  return (
    <div className="container">
      <h2>Teams</h2>
      {(user?.role !== 'MEMBER') && (
        <div className="card">
          <form onSubmit={create} className="row">
            <div className="col">
              <label>Name</label>
              <input value={t.name} onChange={e => setT({...t, name: e.target.value})} />
            </div>
            <div className="col">
              <label>Member IDs (comma-separated)</label>
              <input value={t.memberIds} onChange={e => setT({...t, memberIds: e.target.value})} />
            </div>
            <div className="col">
              <button type="submit">Create</button>
            </div>
          </form>
        </div>
      )}
      <div className="card">
        <table className="table">
          <thead><tr><th>Name</th><th>Members</th></tr></thead>
          <tbody>
            {list.map(team => (
              <tr key={team.id}>
                <td>{team.name}</td>
                <td>{(team.memberIds || []).join(', ')}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}
