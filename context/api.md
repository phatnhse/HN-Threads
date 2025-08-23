# Hacker News API Specification

**Base URL**: `https://hacker-news.firebaseio.com/v0/`  
**Format**: JSON  
**Rate Limit**: None

## Endpoints

### Items
`GET /item/{id}.json`

**Item Types**: `story`, `comment`, `job`, `poll`, `pollopt`

**Fields**:

| Field | Type | Description |
|-------|------|-------------|
| **id** | `number` | Unique identifier |
| **type** | `string` | `"story"`, `"comment"`, `"job"`, `"poll"`, `"pollopt"` |
| by | `string` | Username of author |
| time | `number` | Unix timestamp |
| title | `string` | Title (HTML) |
| text | `string` | Content text (HTML) |
| url | `string` | Story URL |
| score | `number` | Score/votes |
| kids | `number[]` | Child comment IDs (ranked order) |
| parent | `number` | Parent item ID |
| descendants | `number` | Total comment count |
| deleted | `boolean` | Item is deleted |
| dead | `boolean` | Item is dead |
| poll | `number` | Associated poll ID |
| parts | `number[]` | Poll option IDs |

**Example Story**:
```json
{
  "id": 8863,
  "type": "story",
  "by": "dhouston",
  "time": 1175714200,
  "title": "My YC app: Dropbox - Throw away your USB drive",
  "url": "http://www.getdropbox.com/u/2/screencast.html",
  "score": 111,
  "descendants": 71,
  "kids": [8952, 9224, 8917]
}
```

### Users
`GET /user/{id}.json`

**Fields**:
| Field | Type | Description |
|-------|------|-------------|
| **id** | `string` | Username (case-sensitive) |
| **created** | `number` | Unix timestamp |
| **karma** | `number` | User karma score |
| about | `string` | Bio (HTML) |
| submitted | `number[]` | Submitted item IDs |

### Story Lists
| Endpoint | Returns | Limit |
|----------|---------|-------|
| `GET /topstories.json` | Top stories (includes jobs) | 500 |
| `GET /newstories.json` | New stories | 500 |
| `GET /beststories.json` | Best stories | 500 |
| `GET /askstories.json` | Ask HN stories | 200 |
| `GET /showstories.json` | Show HN stories | 200 |
| `GET /jobstories.json` | Job stories | 200 |

**Response**: Array of item IDs
```json
[9129911, 9129199, 9127761]
```

### Utility
| Endpoint | Description | Response |
|----------|-------------|----------|
| `GET /maxitem.json` | Highest item ID | `number` |
| `GET /updates.json` | Changed items/profiles | `{items: number[], profiles: string[]}` |