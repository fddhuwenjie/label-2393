<template>
  <div class="profile-page">
    <div class="container">
      <!-- 用户信息卡片 -->
      <div class="user-card">
        <el-avatar :src="user.avatar" :size="80" />
        <div class="user-info">
          <h2>{{ user.nickname || user.username }}</h2>
          <p>@{{ user.username }}</p>
        </div>
        <div class="stats">
          <div class="stat-item">
            <span class="num">{{ reviews.length }}</span>
            <span class="label">评价</span>
          </div>
          <div class="stat-item">
            <span class="num">{{ ratedMovies.length }}</span>
            <span class="label">看过</span>
          </div>
        </div>
      </div>

      <!-- 标签页 -->
      <el-tabs v-model="activeTab" class="content-tabs">
        <el-tab-pane label="我评价过的电影" name="movies">
          <div class="movie-grid" v-if="ratedMovies.length > 0">
            <div 
              v-for="review in ratedMovies" 
              :key="review.id" 
              class="movie-item"
              @click="goToMovie(review.movieId)"
            >
              <img :src="review.moviePoster || defaultPoster" :alt="review.movieTitle" class="poster" />
              <div class="info">
                <h4>{{ review.movieTitle }}</h4>
                <el-rate :value="review.rating" disabled size="small" />
                <p class="time">{{ formatTime(review.createTime) }}</p>
              </div>
            </div>
          </div>
          <el-empty v-else description="还没有评价过电影" />
        </el-tab-pane>
        
        <el-tab-pane label="我的评论" name="reviews">
          <div class="review-list" v-if="reviews.length > 0">
            <div v-for="review in reviews" :key="review.id" class="review-item">
              <div class="review-movie" @click="goToMovie(review.movieId)">
                <img :src="review.moviePoster || defaultPoster" :alt="review.movieTitle" class="mini-poster" />
                <span class="movie-title">{{ review.movieTitle }}</span>
              </div>
              <div class="review-content">
                <div class="rating-row">
                  <el-rate :value="review.rating" disabled />
                  <span class="time">{{ formatTime(review.createTime) }}</span>
                </div>
                <p class="content">{{ review.content || '(无文字评论)' }}</p>
              </div>
            </div>
          </div>
          <el-empty v-else description="还没有发表过评论" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import { reviewApi } from '@/api'
import { mapState } from 'vuex'

export default {
  name: 'Profile',
  data() {
    return {
      activeTab: 'movies',
      reviews: [],
      defaultPoster: 'https://via.placeholder.com/100x140?text=No+Image'
    }
  },
  computed: {
    ...mapState(['user']),
    ratedMovies() {
      // 去重，每部电影只显示一次
      const movieMap = new Map()
      this.reviews.forEach(r => {
        if (!movieMap.has(r.movieId)) {
          movieMap.set(r.movieId, r)
        }
      })
      return Array.from(movieMap.values())
    }
  },
  created() {
    if (this.user) {
      this.loadReviews()
    }
  },
  methods: {
    async loadReviews() {
      try {
        const res = await reviewApi.getByUserId(this.user.id, { page: 1, size: 100 })
        this.reviews = res.data?.list || []
      } catch (e) {
        this.$message.error('加载数据失败')
      }
    },
    goToMovie(id) {
      this.$router.push(`/movie/${id}`)
    },
    formatTime(time) {
      if (!time) return ''
      return time.replace('T', ' ').substring(0, 10)
    }
  }
}
</script>

<style scoped>
.profile-page {
  max-width: 900px;
  margin: 0 auto;
}

.user-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  gap: 25px;
  margin-bottom: 25px;
  box-shadow: 0 15px 40px rgba(102, 126, 234, 0.3);
  color: white;
}

.user-card >>> .el-avatar {
  border: 4px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
}

.user-info h2 {
  margin: 0;
  font-size: 28px;
  font-weight: 700;
}

.user-info p {
  color: rgba(255, 255, 255, 0.7);
  margin: 8px 0 0;
  font-size: 15px;
}

.stats {
  margin-left: auto;
  display: flex;
  gap: 50px;
}

.stat-item {
  text-align: center;
}

.stat-item .num {
  display: block;
  font-size: 36px;
  font-weight: 800;
}

.stat-item .label {
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
  margin-top: 5px;
}

.content-tabs {
  background: white;
  padding: 30px;
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
}

.content-tabs >>> .el-tabs__header {
  margin-bottom: 25px;
}

.content-tabs >>> .el-tabs__item {
  font-size: 16px;
  font-weight: 500;
}

.content-tabs >>> .el-tabs__item.is-active {
  color: #667eea;
}

.content-tabs >>> .el-tabs__active-bar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.movie-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 20px;
  padding: 10px 0;
}

.movie-item {
  cursor: pointer;
  transition: transform 0.3s;
}

.movie-item:hover {
  transform: scale(1.05);
}

.movie-item .poster {
  width: 100%;
  aspect-ratio: 2/3;
  object-fit: cover;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.movie-item .info {
  padding: 12px 0;
}

.movie-item h4 {
  margin: 0 0 8px;
  font-size: 14px;
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #2d3436;
}

.movie-item .time {
  color: #b2bec3;
  font-size: 12px;
  margin: 8px 0 0;
}

.review-list {
  padding: 10px 0;
}

.review-item {
  display: flex;
  gap: 25px;
  padding: 25px 0;
  border-bottom: 1px solid #e9ecef;
  transition: background 0.3s;
}

.review-item:last-child {
  border-bottom: none;
}

.review-movie {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100px;
  cursor: pointer;
}

.mini-poster {
  width: 80px;
  height: 112px;
  object-fit: cover;
  border-radius: 10px;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s;
}

.review-movie:hover .mini-poster {
  transform: scale(1.05);
}

.movie-title {
  font-size: 12px;
  text-align: center;
  margin-top: 10px;
  color: #636e72;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 100%;
  font-weight: 500;
}

.review-content {
  flex: 1;
}

.rating-row {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 12px;
}

.rating-row .time {
  color: #b2bec3;
  font-size: 13px;
}

.content {
  color: #636e72;
  line-height: 1.8;
  font-size: 15px;
}

@media (max-width: 600px) {
  .user-card {
    flex-direction: column;
    text-align: center;
    padding: 30px 20px;
  }
  
  .stats {
    margin-left: 0;
    margin-top: 20px;
  }
}
</style>
