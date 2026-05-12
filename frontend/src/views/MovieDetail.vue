<template>
  <div class="movie-detail" v-loading="loading">
    <div class="container" v-if="movie">
      <!-- 电影信息 -->
      <div class="movie-header">
        <div class="poster-section">
          <LazyImage 
            :src="movie.poster || defaultPoster" 
            :alt="movie.title" 
            aspect-ratio="140%"
            class="poster"
          />
        </div>
        <div class="info-section">
          <h1 class="title">{{ movie.title }}</h1>
          <div class="rating-display">
            <div class="score">{{ movie.avgRating || '暂无' }}</div>
            <div class="stars">
              <el-rate :value="movie.avgRating" disabled show-score text-color="#ff9900" />
            </div>
            <div class="count">{{ movie.ratingCount }}人评价</div>
          </div>
          <div class="meta-list">
            <p><span class="label">导演:</span> {{ movie.director }}</p>
            <p><span class="label">主演:</span> {{ movie.actors }}</p>
            <p><span class="label">类型:</span> {{ movie.genre }}</p>
            <p><span class="label">地区:</span> {{ movie.region }}</p>
            <p><span class="label">上映:</span> {{ movie.releaseDate }}</p>
            <p><span class="label">时长:</span> {{ movie.duration }}分钟</p>
          </div>
          <div class="description">
            <h3>剧情简介</h3>
            <p>{{ movie.description }}</p>
          </div>
        </div>
      </div>

      <!-- 评分评论区 -->
      <div class="review-section">
        <h2>评分与评论</h2>
        
        <!-- 写评论 -->
        <div class="write-review" v-if="user">
          <h3>{{ myReview ? '修改我的评价' : '写评价' }}</h3>
          <div class="rating-input">
            <span>我的评分:</span>
            <el-rate v-model="reviewForm.rating" show-text :texts="['很差', '较差', '一般', '推荐', '力荐']" />
          </div>
          <el-input
            type="textarea"
            v-model="reviewForm.content"
            :rows="4"
            placeholder="写下你的评论..."
            maxlength="500"
            show-word-limit
          />
          <el-button type="primary" @click="submitReview" :loading="submitting">
            {{ myReview ? '更新评价' : '发表评价' }}
          </el-button>
        </div>
        <div class="login-tip" v-else>
          <el-button type="primary" @click="$router.push('/login')">登录后评价</el-button>
        </div>

        <!-- 评论列表 -->
        <div class="review-list">
          <div class="list-header">
            <h3>全部评论 ({{ reviews.length }})</h3>
            <div class="sort-tabs">
              <span 
                class="sort-tab" 
                :class="{ active: sortBy === 'latest' }"
                @click="changeSort('latest')"
              >最新</span>
              <span 
                class="sort-tab" 
                :class="{ active: sortBy === 'hottest' }"
                @click="changeSort('hottest')"
              >最热</span>
            </div>
          </div>
          <div v-if="reviews.length === 0" class="empty">
            <el-empty description="暂无评论，快来抢沙发吧！" />
          </div>
          <div v-for="review in reviews" :key="review.id" class="review-item">
            <div class="review-header">
              <el-avatar :src="review.avatar" :size="40" />
              <div class="user-info">
                <span class="nickname">{{ review.nickname || review.username }}</span>
                <el-rate :value="review.rating" disabled />
              </div>
              <span class="time">{{ formatTime(review.createTime) }}</span>
            </div>
            <div class="review-content">{{ review.content }}</div>
            <div class="review-actions">
              <span 
                class="like-btn" 
                :class="{ liked: review.liked }"
                @click="toggleLike(review)"
              >
                <i :class="review.liked ? 'el-icon-thumb' : 'el-icon-thumb-empty'"></i>
                <span>{{ review.likeCount || 0 }}</span>
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { movieApi, reviewApi } from '@/api'
import { mapState } from 'vuex'
import LazyImage from '@/components/LazyImage.vue'

export default {
  name: 'MovieDetail',
  components: {
    LazyImage
  },
  data() {
    return {
      movie: null,
      reviews: [],
      myReview: null,
      reviewForm: {
        rating: 5,
        content: ''
      },
      loading: false,
      submitting: false,
      defaultPoster: '/images/default-poster.jpg',
      sortBy: 'latest'
    }
  },
  computed: {
    ...mapState(['user']),
    movieId() {
      return this.$route.params.id
    }
  },
  created() {
    if (this.movieId) {
      this.loadMovie()
      this.loadReviews()
      if (this.user) {
        this.checkMyReview()
      }
    }
  },
  watch: {
    movieId(newId) {
      if (newId) {
        this.loadMovie()
        this.loadReviews()
        if (this.user) {
          this.checkMyReview()
        }
      }
    },
    user(val) {
      if (val) {
        this.checkMyReview()
      }
    }
  },
  methods: {
    /**
     * 加载电影详情信息
     * @returns {Promise<void>}
     */
    async loadMovie() {
      if (!this.movieId) return
      this.loading = true
      try {
        const res = await movieApi.getById(this.movieId)
        this.movie = res.data
      } catch (e) {
        this.$message.error('加载电影信息失败')
      } finally {
        this.loading = false
      }
    },
    /**
     * 加载评论列表
     * @returns {Promise<void>}
     */
    async loadReviews() {
      if (!this.movieId) return
      try {
        const res = await reviewApi.getByMovieId(this.movieId, { 
          page: 1, 
          size: 50,
          sortBy: this.sortBy
        })
        this.reviews = res.data?.list || []
      } catch (e) {
        console.error('加载评论失败', e)
      }
    },
    /**
     * 切换评论排序方式
     * @param {string} sortType - 排序方式：latest(最新) / hottest(最热)
     * @returns {Promise<void>}
     */
    async changeSort(sortType) {
      this.sortBy = sortType
      await this.loadReviews()
    },
    /**
     * 点赞/取消点赞评论
     * @param {Object} review - 评论对象
     * @param {number} review.id - 评论ID
     * @returns {Promise<void>}
     */
    async toggleLike(review) {
      if (!this.user) {
        this.$message.warning('请先登录')
        return
      }
      try {
        const res = await reviewApi.toggleLike(review.id)
        review.liked = res.data.liked
        review.likeCount = res.data.likeCount
      } catch (e) {
        this.$message.error(e.message || '操作失败')
      }
    },
    /**
     * 检查当前用户是否已评价该电影
     * @returns {Promise<void>}
     */
    async checkMyReview() {
      if (!this.user || !this.movieId) return
      try {
        const res = await reviewApi.checkUserReview(this.user.id, this.movieId)
        if (res.data) {
          this.myReview = res.data
          this.reviewForm.rating = res.data.rating
          this.reviewForm.content = res.data.content || ''
        }
      } catch (e) {
        console.error('检查评论失败', e)
      }
    },
    /**
     * 提交评论（新增或更新）
     * @returns {Promise<void>}
     */
    async submitReview() {
      if (!this.movieId) return
      if (this.reviewForm.rating < 1) {
        this.$message.warning('请选择评分')
        return
      }
      
      this.submitting = true
      try {
        await reviewApi.save({
          movieId: this.movieId,
          rating: this.reviewForm.rating,
          content: this.reviewForm.content
        })
        this.$message.success(this.myReview ? '评价已更新' : '评价成功')
        this.loadMovie()
        this.loadReviews()
        this.checkMyReview()
      } catch (e) {
        this.$message.error(e.message || '提交失败')
      } finally {
        this.submitting = false
      }
    },
    /**
     * 格式化时间显示
     * @param {string} time - 原始时间字符串
     * @returns {string} 格式化后的时间字符串
     */
    formatTime(time) {
      if (!time) return ''
      return time.replace('T', ' ').substring(0, 16)
    }
  }
}
</script>

<style scoped>
.movie-detail {
  max-width: 1000px;
  margin: 0 auto;
}

.movie-header {
  display: flex;
  gap: 40px;
  background: white;
  padding: 40px;
  border-radius: 20px;
  margin-bottom: 25px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
}

.poster-section .poster {
  width: 280px;
  border-radius: 16px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  overflow: hidden;
}

.info-section {
  flex: 1;
}

.title {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 20px;
  color: #2d3436;
}

.rating-display {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 25px;
  padding: 20px 25px;
  background: linear-gradient(135deg, #fff9e6 0%, #fff3cd 100%);
  border-radius: 16px;
  border: 1px solid #ffeeba;
}

.score {
  font-size: 48px;
  font-weight: 800;
  background: linear-gradient(135deg, #f6d365 0%, #fda085 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.count {
  color: #856404;
  font-size: 14px;
  font-weight: 500;
}

.meta-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px 30px;
  margin-bottom: 25px;
}

.meta-list p {
  margin: 0;
  color: #636e72;
  font-size: 15px;
}

.label {
  color: #b2bec3;
  margin-right: 8px;
  font-weight: 500;
}

.description {
  margin-top: 25px;
  padding-top: 25px;
  border-top: 1px solid #e9ecef;
}

.description h3 {
  margin-bottom: 15px;
  color: #2d3436;
  font-size: 18px;
  font-weight: 600;
}

.description p {
  color: #636e72;
  line-height: 1.9;
  font-size: 15px;
}

.review-section {
  background: white;
  padding: 40px;
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.08);
}

.review-section h2 {
  margin-bottom: 25px;
  padding-bottom: 20px;
  border-bottom: 2px solid #e9ecef;
  font-size: 22px;
  color: #2d3436;
}

.write-review {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  padding: 30px;
  border-radius: 16px;
  margin-bottom: 35px;
}

.write-review h3 {
  margin-bottom: 20px;
  color: #2d3436;
  font-size: 18px;
}

.rating-input {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
  font-weight: 500;
  color: #636e72;
}

.write-review >>> .el-textarea__inner {
  border-radius: 12px;
  border: 2px solid #e9ecef;
  padding: 15px;
  font-size: 15px;
}

.write-review >>> .el-textarea__inner:focus {
  border-color: #667eea;
}

.write-review .el-button {
  margin-top: 20px;
  padding: 12px 30px;
  border-radius: 25px;
}

.login-tip {
  text-align: center;
  padding: 50px 30px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 16px;
  margin-bottom: 35px;
}

.login-tip .el-button {
  padding: 12px 40px;
  border-radius: 25px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.list-header h3 {
  margin: 0;
  color: #2d3436;
  font-size: 18px;
}

.sort-tabs {
  display: flex;
  gap: 20px;
}

.sort-tab {
  cursor: pointer;
  color: #b2bec3;
  font-size: 15px;
  font-weight: 500;
  transition: all 0.3s;
  position: relative;
}

.sort-tab:hover {
  color: #667eea;
}

.sort-tab.active {
  color: #667eea;
}

.sort-tab.active::after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 100%;
  height: 2px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 1px;
}

.review-item {
  padding: 25px 0;
  border-bottom: 1px solid #e9ecef;
  transition: background 0.3s;
}

.review-item:last-child {
  border-bottom: none;
}

.review-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
}

.review-header >>> .el-avatar {
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.user-info {
  flex: 1;
}

.nickname {
  display: block;
  font-weight: 600;
  margin-bottom: 6px;
  color: #2d3436;
}

.time {
  color: #b2bec3;
  font-size: 13px;
}

.review-content {
  color: #636e72;
  line-height: 1.9;
  padding-left: 55px;
  font-size: 15px;
}

.review-actions {
  padding-left: 55px;
  margin-top: 15px;
}

.like-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  color: #b2bec3;
  font-size: 14px;
  padding: 8px 12px;
  border-radius: 20px;
  background: #f8f9fa;
  transition: all 0.3s;
  user-select: none;
}

.like-btn:hover {
  color: #667eea;
  background: #eef1ff;
}

.like-btn.liked {
  color: #e74c3c;
  background: #fdecea;
}

.like-btn i {
  font-size: 16px;
}

.empty {
  padding: 50px;
}

@media (max-width: 768px) {
  .movie-header {
    flex-direction: column;
    align-items: center;
    padding: 30px 20px;
  }
  
  .poster-section .poster {
    width: 220px;
  }
  
  .meta-list {
    grid-template-columns: 1fr;
  }
  
  .review-section {
    padding: 25px 20px;
  }
}
</style>
