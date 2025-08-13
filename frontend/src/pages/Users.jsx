import React, { useEffect, useState } from 'react'
import api from '../api'
import { useAuth } from '../context/AuthContext'

export default function Users() {
  const { user, loading } = useAuth()
  const [list, setList] = useState([])
  const [n, setN] = useState({ name:'', email:'', password:'', role:'MEMBER' })
  const [pageLoading, setPageLoading] = useState(true)

  useEffect(() => {
    if (loading || !user) return
    setPageLoading(true)
    api.get('/api/users')
      .then(r => setList(r.data))
      .catch(console.error)
      .finally(() => setPageLoading(false))
  }, [loading, user])

  const create = async e => {
    e.preventDefault()
    await api.post('/api/users', n)
    setN({ name:'', email:'', password:'', role:'MEMBER' })
    // reload list
    setPageLoading(true)
    api.get('/api/users')
      .then(r => setList(r.data))
      .catch(console.error)
      .finally(() => setPageLoading(false))
  }

  const changeRole = async (id, role) => {
    await api.patch(`/api/users/${id}/role`, { role })
    load()
  }
  const del = async id => {
    await api.delete(`/api/users/${id}`)
    load()
  }
  const load = () => {
    setPageLoading(true)
    api.get('/api/users')
      .then(r => setList(r.data))
      .catch(console.error)
      .finally(() => setPageLoading(false))
  }

  if (loading || pageLoading) {
    return <div style={{ textAlign: 'center', padding: '2rem' }}>Loading users...</div>
  }

  if (user?.role !== 'ADMIN') {
    return <div style={{ textAlign: 'center', padding: '2rem' }}>Access denied.</div>
  }

  return (
    <div className="container">
      <h2>Users</h2>
      <div className="card">
        <form onSubmit={create} className="row">
          <div className="col"><label>Name</label><input value={n.name} onChange={e=>setN({...n,name:e.target.value})} /></div>
          <div className="col"><label>Email</label><input value={n.email} onChange={e=>setN({...n,email:e.target.value})} /></div>
          <div className="col"><label>Password</label><input value={n.password} onChange={e=>setN({...n,password:e.target.value})} /></div>
          <div className="col">
            <label>Role</label>
            <select value={n.role} onChange={e=>setN({...n,role:e.target.value})}>
              <option>ADMIN</option><option>MANAGER</option><option>MEMBER</option>
            </select>
          </div>
          <div className="col"><button type="submit">Create</button></div>
        </form>
      </div>
      <div className="card">
        <table className="table">
          <thead><tr><th>Name</th><th>Email</th><th>Role</th><th></th></tr></thead>
          <tbody>
            {list.map(u => (
              <tr key={u.id}>
                <td>{u.name}</td><td>{u.email}</td>
                <td>
                  <select value={u.role} onChange={e=>changeRole(u.id, e.target.value)}>
                    <option>ADMIN</option><option>MANAGER</option><option>MEMBER</option>
                  </select>
                </td>
                <td><button onClick={()=>del(u.id)}>Delete</button></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}
