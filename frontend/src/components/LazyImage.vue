<template>
  <div class="lazy-image-wrapper" :style="wrapperStyle">
    <!-- 骨架屏 -->
    <div v-if="!loaded" class="skeleton">
      <div class="skeleton-shimmer"></div>
    </div>
    <!-- 实际图片 -->
    <img
      v-show="loaded"
      ref="img"
      :src="currentSrc"
      :alt="alt"
      class="lazy-image"
      :class="{ 'fade-in': loaded }"
      @load="onLoad"
      @error="onError"
    />
  </div>
</template>

<script>
export default {
  name: 'LazyImage',
  props: {
    src: {
      type: String,
      required: true
    },
    alt: {
      type: String,
      default: ''
    },
    aspectRatio: {
      type: String,
      default: '140%' // 海报比例
    },
    placeholder: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loaded: false,
      error: false,
      observer: null,
      currentSrc: ''
    }
  },
  computed: {
    wrapperStyle() {
      return {
        paddingTop: this.aspectRatio
      }
    }
  },
  mounted() {
    this.initObserver()
  },
  beforeDestroy() {
    if (this.observer) {
      this.observer.disconnect()
    }
  },
  methods: {
    initObserver() {
      // 使用 IntersectionObserver 实现懒加载
      if ('IntersectionObserver' in window) {
        this.observer = new IntersectionObserver(
          (entries) => {
            entries.forEach((entry) => {
              if (entry.isIntersecting) {
                this.loadImage()
                this.observer.disconnect()
              }
            })
          },
          {
            rootMargin: '50px 0px', // 提前50px开始加载
            threshold: 0.01
          }
        )
        this.observer.observe(this.$el)
      } else {
        // 降级处理：直接加载
        this.loadImage()
      }
    },
    loadImage() {
      this.currentSrc = this.src
    },
    onLoad() {
      this.loaded = true
      this.$emit('load')
    },
    onError() {
      this.error = true
      this.currentSrc = this.placeholder || 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 300 420"%3E%3Crect fill="%23f0f0f0" width="300" height="420"/%3E%3Ctext x="150" y="210" text-anchor="middle" fill="%23999" font-size="14"%3E暂无图片%3C/text%3E%3C/svg%3E'
      this.loaded = true
      this.$emit('error')
    }
  }
}
</script>

<style scoped>
.lazy-image-wrapper {
  position: relative;
  width: 100%;
  overflow: hidden;
  background: #f5f5f5;
}

.skeleton {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.skeleton-shimmer {
  width: 100%;
  height: 100%;
}

@keyframes shimmer {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}

.lazy-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.lazy-image.fade-in {
  opacity: 1;
}
</style>
