import React, { useEffect, useState } from 'react'
import api from '../api'
import { Link } from 'react-router-dom'
import SearchFilter from '../components/SearchFilter'
import { useAuth } from '../context/AuthContext'

export default function Tasks() {
  const { user, loading } = useAuth()
  const [list, setList] = useState([])
  const [filters, setFilters] = useState({ status:'', priority:'', q:'' })
  const [pageLoading, setPageLoading] = useState(true)

  useEffect(() => {
    if (loading || !user) return; // wait for auth ready
    setPageLoading(true)
    api.get('/api/tasks')
      .then(r => setList(r.data))
      .catch(console.error)
      .finally(() => setPageLoading(false))
  }, [loading, user])

  if (loading || pageLoading) {
    return <div style={{ textAlign: 'center', padding: '2rem' }}>Loading tasks...</div>
  }

  const filtered = list.filter(t =>
    (!filters.status || t.status === filters.status) &&
    (!filters.priority || t.priority === filters.priority) &&
    (!filters.q || (t.title?.toLowerCase().includes(filters.q.toLowerCase()) || t.description?.toLowerCase().includes(filters.q.toLowerCase())))
  )

  return (
    <div className="container">
      <div className="row">
        <h2 style={{ flex:1 }}>Tasks</h2>
        {(user?.role !== 'MEMBER') && <Link className="badge" to="/tasks/new">+ New Task</Link>}
        <select value={filters.status} onChange={e => setFilters({...filters, status: e.target.value})}>
          <option value="">All Status</option><option>TODO</option><option>IN_PROGRESS</option><option>DONE</option>
        </select>
        <select value={filters.priority} onChange={e => setFilters({...filters, priority: e.target.value})}>
          <option value="">All Priority</option><option>LOW</option><option>MEDIUM</option><option>HIGH</option>
        </select>
        <SearchFilter value={filters.q} onChange={v => setFilters({...filters, q: v})} />
      </div>
      <div className="card">
        <table className="table">
          <thead><tr><th>Title</th><th>Status</th><th>Priority</th><th>Assignee</th><th>Project</th><th></th></tr></thead>
          <tbody>
            {filtered.map(t => (
              <tr key={t.id}>
                <td>{t.title}</td>
                <td><span className="badge">{t.status}</span></td>
                <td>{t.priority}</td>
                <td>{t.assigneeId || '-'}</td>
                <td>{t.projectId}</td>
                <td><Link to={`/tasks/${t.id}`}>Open</Link></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}
