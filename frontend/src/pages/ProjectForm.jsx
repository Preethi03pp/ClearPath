import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import api from '../api'

export default function ProjectForm() {
  const { id } = useParams()
  const [p, setP] = useState({ name:'', description:'', status:'ACTIVE', managerId:'', teamId:'' })
  const [teams, setTeams] = useState([])
  const nav = useNavigate()

  useEffect(() => {
    api.get('/api/teams').then(r => setTeams(r.data)).catch(console.error)
    if (id !== 'new') {
      api.get(`/api/projects/${id}`)
        .then(r => setP(r.data))
        .catch(err => {
          console.error('Failed to fetch project:', err)
          alert('Failed to load project data')
        })
    }
  }, [id])

  const save = async e => {
    e.preventDefault()
    if (!p.name.trim()) {
      alert('Name is required')
      return
    }
    try {
      if (id === 'new') await api.post('/api/projects', p)
      else await api.put(`/api/projects/${id}`, p)
      nav('/projects')
    } catch (error) {
      console.error('Failed to save project:', error)
      alert('Failed to save project: ' + (error.response?.data?.message || error.message))
    }
  }

  return (
    <div className="container">
      <div className="card">
        <h2>{id === 'new' ? 'New' : 'Edit'} Project</h2>
        <form onSubmit={save} className="row">
          <div className="col">
            <label>Name</label>
            <input value={p.name} onChange={e => setP({ ...p, name: e.target.value })} required />
          </div>
          <div className="col">
            <label>Status</label>
            <select value={p.status} onChange={e => setP({ ...p, status: e.target.value })}>
              <option>ACTIVE</option><option>COMPLETED</option><option>ARCHIVED</option>
            </select>
          </div>
          <div className="col">
            <label>Team</label>
            <select value={p.teamId || ''} onChange={e => setP({ ...p, teamId: e.target.value })}>
              <option value="">(none)</option>
              {teams.map(t => <option key={t.id} value={t.id}>{t.name}</option>)}
            </select>
          </div>
          <div className="col" style={{ flexBasis: '100%' }}>
            <label>Description</label>
            <textarea value={p.description} onChange={e => setP({ ...p, description: e.target.value })} />
          </div>
          <div className="col">
            <button type="submit">Save</button>
          </div>
        </form>
      </div>
    </div>
  )
}
