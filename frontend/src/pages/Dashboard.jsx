import React, { useEffect, useState } from 'react'
import api from '../api'
import { useAuth } from '../context/AuthContext'

export default function Dashboard() {
  const { user } = useAuth()
  const [data, setData] = useState(null)
  useEffect(() => { api.get('/api/analytics').then(r => setData(r.data)) }, [])
  if (!data) return <div className="container"><div className="card">Loading...</div></div>

  return (
    <div className="container">
      <h2>Analytics</h2>
      <div className="row">
        <div className="card col"><h3>Total Projects</h3><h1>{data.totalProjects}</h1></div>
        <div className="card col"><h3>Total Tasks</h3><h1>{data.totalTasks}</h1></div>
        <div className="card col"><h3>Your Role</h3><h1 className="badge">{user.role}</h1></div>
      </div>
      <div className="row">
        <div className="card col"><h3>Tasks by Status</h3>{Object.entries(data.tasksByStatus || {}).map(([k,v]) => <div key={k}>{k}: {v}</div>)}</div>
        <div className="card col"><h3>Tasks by Priority</h3>{Object.entries(data.tasksByPriority || {}).map(([k,v]) => <div key={k}>{k}: {v}</div>)}</div>
        <div className="card col"><h3>Tasks per Assignee</h3>{Object.entries(data.tasksPerAssignee || {}).map(([k,v]) => <div key={k}>{k}: {v}</div>)}</div>
      </div>
    </div>
  )
}
