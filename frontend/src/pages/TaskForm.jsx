import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import api from '../api'
import FileUploader from '../components/FileUploader'
import { useAuth } from '../context/AuthContext'

export default function TaskForm() {
  const { id } = useParams()
  const [t, setT] = useState({ title:'', description:'', projectId:'', status:'TODO', priority:'MEDIUM', assigneeId:'' })
  const [projects, setProjects] = useState([])
  const [users, setUsers] = useState([])
  const nav = useNavigate()
  const { user } = useAuth()

  useEffect(() => {
    api.get('/api/projects').then(r => setProjects(r.data))
    if (user.role === 'ADMIN') api.get('/api/users').then(r => setUsers(r.data))
    else api.get('/api/auth/me').then(r => setUsers([r.data]))
    if (id !== 'new') {
      api.get('/api/tasks').then(r => {
        const found = r.data.find(x => x.id === id)
        if (found) setT(found)
      })
    }
  }, [id])

  const save = async e => {
    e.preventDefault()
    if (id === 'new') await api.post('/api/tasks', t)
    else await api.put(`/api/tasks/${id}`, t)
    nav('/tasks')
  }

  const onUploaded = meta => {
    setT(prev => ({ ...prev, attachments: [...(prev.attachments||[]), meta] }))
  }

  return (
    <div className="container">
      <div className="card">
        <h2>{id === 'new' ? 'New' : 'Edit'} Task</h2>
        <form onSubmit={save} className="row">
          <div className="col"><label>Title</label><input value={t.title} onChange={e=>setT({...t,title:e.target.value})} /></div>
          <div className="col">
            <label>Status</label>
            <select value={t.status} onChange={e=>setT({...t,status:e.target.value})}>
              <option>TODO</option><option>IN_PROGRESS</option><option>DONE</option>
            </select>
          </div>
          <div className="col">
            <label>Priority</label>
            <select value={t.priority} onChange={e=>setT({...t,priority:e.target.value})}>
              <option>LOW</option><option>MEDIUM</option><option>HIGH</option>
            </select>
          </div>
          <div className="col">
            <label>Project</label>
            <select value={t.projectId || ''} onChange={e=>setT({...t,projectId:e.target.value})}>
              <option value="">(select)</option>
              {projects.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
            </select>
          </div>
          <div className="col">
            <label>Assignee</label>
            <input placeholder="User ID" value={t.assigneeId || ''} onChange={e=>setT({...t,assigneeId:e.target.value})} />
          </div>
          <div className="col" style={{flexBasis:'100%'}}>
            <label>Description</label>
            <textarea value={t.description} onChange={e=>setT({...t,description:e.target.value})}/>
          </div>
          <div className="col"><button type="submit">Save</button></div>
        </form>
      </div>

      {id !== 'new' && (
        <div className="card">
          <h3>Attachments</h3>
          <FileUploader taskId={id} onUploaded={onUploaded} />
          <ul>
            {(t.attachments || []).map(a => (
              <li key={a.id}><a href={import.meta.env.VITE_API_BASE ? import.meta.env.VITE_API_BASE + a.url : 'https://clearpath.onrender.com' + a.url} target="_blank">{a.originalName}</a></li>
            ))}
          </ul>
        </div>
      )}
    </div>
  )
}
