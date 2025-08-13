import React, { useState } from 'react'
import api from '../api'

export default function FileUploader({ taskId, onUploaded }) {
  const [loading, setLoading] = useState(false)
  const [err, setErr] = useState(null)

  const upload = async e => {
    const file = e.target.files?.[0]
    if (!file) return
    setLoading(true); setErr(null)
    const fd = new FormData()
    fd.append('file', file)
    try {
      const { data } = await api.post(`/api/tasks/${taskId}/attachments`, fd, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      onUploaded?.(data)
    } catch (e) {
      setErr('Upload failed')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div>
      <input type="file" onChange={upload} disabled={loading} />
      {loading && <span>Uploading...</span>}
      {err && <span style={{ color:'tomato' }}>{err}</span>}
    </div>
  )
}
